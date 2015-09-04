__author__ = 'Nischaal Cooray, Cameron Spencer, Roger White'

import requests
from flask import jsonify
import xml.etree.ElementTree as ET

#Use the Meetup.com API (http://www.meetup.com/meetup_api/) to find events in the area and give the user the
#option to add them to the calender

def getSuggestedEvents() :
    toReturn = []

    eventfulUrl = 'http://api.eventful.com/rest/events/search?app_key=NFsshgL9LvmGjjrB&location={0}&sort_order=date' \
                  '&date=2015080300-2015082300&change_multi_day_start=true&page_size=20'.format('philadelphia')
    res = requests.get(eventfulUrl)

    root = ET.fromstring(res.content)

    for event in root.iter('event') :
        current = {}
        current['title'] = event.find('title').text
        current['date_time'] = event.find('start_time').text
        current['venue'] = event.find('venue_name').text
        current['address'] = event.find('venue_address').text
        current['url'] = event.find('url').text
        toReturn.append(current)

    return jsonify(items=toReturn)