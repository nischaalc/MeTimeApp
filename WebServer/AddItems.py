__author__ = 'Nischaal Cooray, Cameron Spencer, Roger White'

from flask import jsonify
import requests
import json
import datetime
import math
import random

#Globals
weekdays = {1: 'Monday', 2: 'Tuesday', 3: 'Wednesday', 4: 'Thursday', 5: 'Friday', 6: 'Saturday', 7: 'Sunday'}

def getCalendar(postData) :
    '''
    Gets the ID of the users' primary calendar
    :param postData: The POST body sent with the request
    :return: The Calendar ID for the user
    '''
    url = 'https://www.googleapis.com/calendar/v3/users/me/calendarList'
    accessToken = postData.get('token')

    res = requests.get(url, headers={'Authorization': 'Bearer ' + accessToken})
    resJson = json.loads(res.text)
    return resJson['items'][0]['id']

def addNewItem(postData) :
    '''
    Determine what type of event is to be added to the calendar
    :param postData: The POST body sent with the request
    '''
    calId = getCalendar(postData)

    response = ''
    itemType = postData.get('itype')

    if itemType == 'Class' :
        response = pushClass(postData, calId)
        detStudyTimes(postData, calId)
    elif itemType == 'Event' :
        response = pushEvent(postData, calId)
    elif itemType == 'Goal' :
        response = pushGoal(postData, calId)

    return response

def pushClass(postData, calId) :
    '''
    Add a Class to the calendar
    :param postData: The POST body sent with the request
    :param calId: The calendar ID for the events to be added to
    '''
    url = 'https://www.googleapis.com/calendar/v3/calendars/{0}/events'.format(calId)

    #Get required information from the POST body
    accessToken = postData.get('token')
    summary = postData.get('title')
    endTime = postData.get('etime')
    startTime = postData.get('stime')
    days = postData.get('dtime').split(',')

    today = datetime.datetime.today()
    recurrenceString = 'RRULE:FREQ=WEEKLY;BYDAY='

    #Determine which days the event should repeat on
    if len(days) > 1 :
        for day in days :
            if day == 'Monday' :
                recurrenceString += 'MO,'
            elif day == 'Tuesday' :
                recurrenceString += 'TU,'
            elif day == 'Wednesday' :
                recurrenceString += 'WE,'
            elif day == 'Thursday' :
                recurrenceString += 'TH,'
            elif day == 'Friday' :
                recurrenceString += 'FR,'
            elif day == 'Saturday' :
                recurrenceString += 'SA,'
            elif day == 'Sunday' :
                recurrenceString += 'SU,'

    #Format the date to match UTC format
    properStartTime = datetime.datetime.strptime(startTime, '%H:%M').time()
    properEndTime = datetime.datetime.strptime(endTime, '%H:%M').time()
    start = datetime.datetime.combine(today.date(), properStartTime)
    end = datetime.datetime.combine(today.date(), properEndTime)

    start = str(start).replace(' ', 'T')
    end = str(end).replace(' ', 'T')

    start = start + '.000Z'
    end = end + '.000Z'

    #Form the post body required by Google
    postbody = {
        'end':{
            'dateTime': end,
            'timeZone': 'US/Eastern'
        },
        'start': {
            'dateTime': start,
            'timeZone': 'US/Eastern'
        },
        'summary': summary,
        'description': 'MeTime.Class',
        'recurrence' : [
            recurrenceString[:-1]
        ]
    }

    #Send the POST request
    res = requests.post(url, json=postbody, headers={'Authorization': 'Bearer ' + accessToken})
    return res.text

def detStudyTimes(postdata, calId) :
    '''
    Determine study times for a class
    :param postData: The POST body sent with the request
    :param calId: The calendar ID for the events to be added to
    '''
    freeUrl = 'https://www.googleapis.com/calendar/v3/freeBusy'

    #Get required fields from the POST body
    accessToken = postdata.get('token')
    goalName = postdata.get('title')
    priority = postdata.get('pri')

    dateMin = datetime.datetime.today()
    dateMax = dateMin + datetime.timedelta(days=6)
    start = str(dateMin).replace(' ', 'T')
    end = str(dateMax).replace(' ', 'T')
    start = start + 'Z'
    end = end + 'Z'

    #Google endpoint that returns free times for a specific calendar
    postbody = {'timeMax': end, 'timeMin': start, 'items': [{'id':calId }]}

    res = requests.post(freeUrl, json=postbody, headers={'Authorization': 'Bearer ' + accessToken})

    resJson = json.loads(res.text)
    busyTimes = resJson['calendars'][calId]['busy']

    minuteArray = [00, 15, 30, 45]
    temp = {}
    counter = 0
    for i in range(0, int(math.ceil(float(priority)))) :
        success = False

        while not success :
            #Format dates so that they work properly! So frustrating...
            hour = random.randrange(10, 22)
            minute = minuteArray[random.randrange(0, 3)]
            day = random.randrange(1,6)
            today = datetime.datetime.today()
            nextDay = today + datetime.timedelta((day - today.isoweekday()) % 7)
            startDateTime = str(nextDay.date()) + 'T' + str(hour) + ':' + str(minute) + ':00Z'
            endDateTime = str(nextDay.date()) + 'T' + str(hour + 1) + ':' + str(minute) + ':00Z'
            startDateTime = datetime.datetime.strptime(startDateTime, '%Y-%m-%dT%H:%M:%SZ')
            endDateTime = datetime.datetime.strptime(endDateTime, '%Y-%m-%dT%H:%M:%SZ')

            for time in busyTimes :
                calStart = time['start']
                calEnd = time['end']
                calStart = datetime.datetime.strptime(calStart, '%Y-%m-%dT%H:%M:%SZ')
                calEnd = datetime.datetime.strptime(calEnd, '%Y-%m-%dT%H:%M:%SZ')

                #Check to see if the computed time clashes with any existing events
                if not (calStart < startDateTime < calEnd) and not (calStart < endDateTime < calEnd) :
                    startDateTime = str(startDateTime).replace(' ', 'T')
                    endDateTime = str(endDateTime).replace(' ', 'T')
                    startDateTime = startDateTime + 'Z'
                    endDateTime = endDateTime + 'Z'

                    #Form the POST body for the request
                    postbody = {
                        'end':{
                            'dateTime': endDateTime
                        },
                        'start': {
                            'dateTime': startDateTime
                        },
                        'summary': 'Study ' + goalName,
                        'description': 'MeTime.Study'
                    }

                    #Send the POST request
                    url = 'https://www.googleapis.com/calendar/v3/calendars/{0}/events'.format(calId)
                    res = requests.post(url, json=postbody, headers={'Authorization': 'Bearer ' + accessToken})
                    counter += 1
                    temp[counter] = res.text
                    success = True
                    break

def pushGoal(postdata, calId) :
    '''
    Add a Goal to the calendar
    :param postData: The POST body sent with the request
    :param calId: The calendar ID for the events to be added to
    '''
    freeUrl = 'https://www.googleapis.com/calendar/v3/freeBusy'

    #Get required information from the POST body
    accessToken = postdata.get('token')
    goalName = postdata.get('title')
    priority = postdata.get('pri')

    dateMin = datetime.datetime.today()
    dateMax = dateMin + datetime.timedelta(days=6)
    start = str(dateMin).replace(' ', 'T')
    end = str(dateMax).replace(' ', 'T')
    start = start + 'Z'
    end = end + 'Z'

    postbody = {'timeMax': end, 'timeMin': start, 'items': [{'id':calId }]}

    #Google endpoint that returns free times for a specific calendar
    res = requests.post(freeUrl, json=postbody, headers={'Authorization': 'Bearer ' + accessToken})

    resJson = json.loads(res.text)
    busyTimes = resJson['calendars'][calId]['busy']

    minuteArray = [00, 15, 30, 45]
    temp = {}
    counter = 0
    for i in range(0, int(math.ceil(float(priority)))) :
        success = False

        while not success :
            #Format dates so that they work properly! So frustrating...
            hour = random.randrange(10, 22)
            minute = minuteArray[random.randrange(0, 3)]
            day = random.randrange(1,6)
            today = datetime.datetime.today()
            nextDay = today + datetime.timedelta((day - today.isoweekday()) % 7)
            startDateTime = str(nextDay.date()) + 'T' + str(hour) + ':' + str(minute) + ':00Z'
            endDateTime = str(nextDay.date()) + 'T' + str(hour + 1) + ':' + str(minute) + ':00Z'
            startDateTime = datetime.datetime.strptime(startDateTime, '%Y-%m-%dT%H:%M:%SZ')
            endDateTime = datetime.datetime.strptime(endDateTime, '%Y-%m-%dT%H:%M:%SZ')

            for time in busyTimes :
                calStart = time['start']
                calEnd = time['end']
                calStart = datetime.datetime.strptime(calStart, '%Y-%m-%dT%H:%M:%SZ')
                calEnd = datetime.datetime.strptime(calEnd, '%Y-%m-%dT%H:%M:%SZ')

                #Check to see if the computed time clashes with any existing events
                if not (calStart < startDateTime < calEnd) and not (calStart < endDateTime < calEnd) :
                    startDateTime = str(startDateTime).replace(' ', 'T')
                    endDateTime = str(endDateTime).replace(' ', 'T')
                    startDateTime = startDateTime + 'Z'
                    endDateTime = endDateTime + 'Z'

                    #Form the POST body for the request
                    postbody = {
                        'end':{
                            'dateTime': endDateTime
                        },
                        'start': {
                            'dateTime': startDateTime
                        },
                        'summary':goalName,
                        'description': 'MeTime.Goal'
                    }

                    #Send the POST request
                    url = 'https://www.googleapis.com/calendar/v3/calendars/{0}/events'.format(calId)
                    res = requests.post(url, json=postbody, headers={'Authorization': 'Bearer ' + accessToken})
                    counter += 1
                    temp[counter] = res.text
                    success = True
                    break

    return jsonify(temp)

def pushEvent(postData, calId) :
    '''
    Add a Event to the calendar
    :param postData: The POST body sent with the request
    :param calId: The calendar ID for the events to be added to
    '''
    url = 'https://www.googleapis.com/calendar/v3/calendars/{0}/events'.format(calId)

    #Get required information from the POST body
    accessToken = postData.get('token')
    summary = postData.get('title')
    endTime = postData.get('etime')
    startTime = postData.get('stime')
    days = postData.get('eday')

    properStartTime = datetime.datetime.strptime(startTime, '%H:%M').time()
    properEndTime = datetime.datetime.strptime(endTime, '%H:%M').time()
    properDate = datetime.datetime.strptime(days, '%Y-%m-%d').date()
    start = datetime.datetime.combine(properDate, properStartTime)
    end = datetime.datetime.combine(properDate, properEndTime)
    start = str(start).replace(' ', 'T')
    end = str(end).replace(' ', 'T')
    start = start + '.000Z'
    end = end + '.000Z'

    postbody = {
        'end':{
            'dateTime': end
        },
        'start': {
            'dateTime': start
        },
        'summary':summary,
        'description': 'MeTime.Event'
    }

    res = requests.post(url, json=postbody, headers={'Authorization': 'Bearer ' + accessToken})
    return res.text