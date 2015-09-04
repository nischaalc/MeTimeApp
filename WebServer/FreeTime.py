__author__ = 'Nischaal Cooray, Cameron Spencer, Roger White'

import requests

import json

# FLOW
# 1) Get access token from URL
# 2) Use access token to request Google calendar data
# 3) ???

freeURL = 'https://www.googleapis.com/calendar/v3/freeBusy'

def freeTime(accessToken) :
    jsonObj = getCalList(accessToken)
    return jsonObj
    #parsed_json = json.loads(jsonObj)
    #return parsed_json

    #getFreeTime()

def getCalList(accessToken) :
    url = 'https://www.googleapis.com/calendar/v3/users/me/calendarList'
    res = requests.get(url, headers={'Authorization': 'Bearer ' + accessToken})

    data = json.loads(res.content)
    return data
    #calendarName = data['items'][0]['id']
    #eturn calendarName

def getFreeTime() :
    print('Free time')