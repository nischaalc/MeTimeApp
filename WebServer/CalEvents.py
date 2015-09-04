__author__ = 'Nischaal Cooray, Cameron Spencer, Roger White'

import requests
import json
from flask import jsonify
import datetime
import pytz

weekdays = {1: 'MO', 2: 'TU', 3: 'WE', 4: 'TH', 5: 'FR', 6: 'SA', 7: 'SU'}

def getCalEvents(accessToken) :
    '''
    Prepare required data in order to get events from the calendar
    :param accessToken: The access token required to access the calendar
    '''
    url = 'https://www.googleapis.com/calendar/v3/users/me/calendarList'
    res = requests.get(url, headers={'Authorization': 'Bearer ' + accessToken})
    resJson = json.loads(res.text)
    calId = resJson['items'][0]['id']
    events = getEvents(calId, accessToken)
    return jsonify(items=events)

def getEvents(calId, accessToken) :
    '''
    Request a list of all events from the calendar
    :param calId: The calendar ID of the calendar that the events are to be fetched from
    :param accessToken: The access token required to access the calendar
    '''
    url = 'https://www.googleapis.com/calendar/v3/calendars/{0}/events?'.format(calId)

    #Form the date range to et events for
    dateMin = datetime.datetime.today()
    dateMax = dateMin + datetime.timedelta(days=6)
    timePeriod = 'timeMax={0}-04:00&timeMin={1}-04:00'.format(dateMax, dateMin)
    timePeriod = timePeriod.replace(' ', 'T')
    url = url + timePeriod

    #Send the GET request
    res = requests.get(url, headers={'Authorization': 'Bearer ' + accessToken})
    resJson = json.loads(res.text)

    itemArray = resJson['items']
    returnArray = []

    for item in itemArray :
        daysList = []
        current = {}

        #Create a formatted response to be returned to the requesting client
        current['title'] = item['summary']
        current['start'] = item['start']['dateTime']
        current['end'] = item['end']['dateTime']
        if item.get('description') :
            current['description'] = item['description']
            if item['description'] == 'MeTime.Class' :
                current['color'] = '#B3001B'
            elif item['description'] == 'MeTime.Event' :
                current['color'] = '#297373'
            elif item['description'] == 'MeTime.Goal' :
                current['color'] = '#1F2041'
            elif item['description'] == 'MeTime.Study' :
                current['color'] = '#BA3233'
        else :
            current['description'] = 'GoogleItem'
        if item.get('recurrence') :
            if 'WEEKLY' in item['recurrence'][0] :
                recurrence = item['recurrence'][0]
                index = recurrence.rfind('=')
                daysList = recurrence[index+1:].split(',')
        returnArray.append(current)

        keys = list(weekdays.keys())
        values = list(weekdays.values())

        #Handle recurring events
        for day in daysList :
            current = {}

            startInd = item['start']['dateTime'].find('T')
            startTime = item['start']['dateTime'][startInd+1:]
            endInd = item['end']['dateTime'].find('T')
            endTime = item['end']['dateTime'][endInd+1:]

            dIndex = keys[values.index(day)]
            today = datetime.datetime.today()
            nextDay = today + datetime.timedelta((dIndex - today.isoweekday()) %7 )

            if nextDay.date() != today.date() :
                current['title'] = item['summary']
                current['start'] = str(nextDay.date()) + 'T' + startTime
                current['end'] = str(nextDay.date()) + 'T' + endTime
                if item.get('description') :
                    current['description'] = item['description']
                    if item['description'] == 'MeTime.Class' :
                        current['color'] = '#B3001B'
                    elif item['description'] == 'MeTime.Event' :
                        current['color'] = '#297373'
                    elif item['description'] == 'MeTime.Goal' :
                        current['color'] = '#1F2041'
                    elif item['description'] == 'MeTime.Study' :
                        current['color'] = '#BA3233'
                else :
                    current['description'] = 'GoogleItem'
                returnArray.append(current)

    return returnArray