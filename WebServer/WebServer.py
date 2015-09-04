__author__ = 'Nischaal Cooray, Cameron Spencer, Roger White'

from flask import Flask
from SuggestedEvents import *
from FreeTime import *
from CalEvents import *
from AddItems import *

from datetime import timedelta
from flask import make_response, request, current_app
from functools import update_wrapper

app = Flask(__name__)

''' This is the main class for the server. Calls to the server are intercepted here and redirected to the classes
    made to handle them.'''

def crossdomain(origin=None, methods=None, headers=None,
                max_age=21600, attach_to_all=True,
                automatic_options=True):
    '''
    Required by Flask to ensure to ensure CORS compatibility
    Found at http://flask.pocoo.org/snippets/56/
    '''
    if methods is not None:
        methods = ', '.join(sorted(x.upper() for x in methods))
    if headers is not None and not isinstance(headers, str):
        headers = ', '.join(x.upper() for x in headers)
    if not isinstance(origin, str):
        origin = ', '.join(origin)
    if isinstance(max_age, timedelta):
        max_age = max_age.total_seconds()

    def get_methods():
        if methods is not None:
            return methods

        options_resp = current_app.make_default_options_response()
        return options_resp.headers['allow']

    def decorator(f):
        def wrapped_function(*args, **kwargs):
            if automatic_options and request.method == 'OPTIONS':
                resp = current_app.make_default_options_response()
            else:
                resp = make_response(f(*args, **kwargs))
            if not attach_to_all and request.method != 'OPTIONS':
                return resp

            h = resp.headers

            h['Access-Control-Allow-Origin'] = origin
            h['Access-Control-Allow-Methods'] = get_methods()
            h['Access-Control-Max-Age'] = str(max_age)
            if headers is not None:
                h['Access-Control-Allow-Headers'] = headers
            return resp

        f.provide_automatic_options = False
        return update_wrapper(wrapped_function, f)
    return decorator

@app.route('/')
def hello_world():
    '''
    Main endpoint of the server. Currently does not do anything.
    '''
    return 'Welcome to MeTime!'

@app.route('/getEvents')
@crossdomain(origin='*')
def GetEvents() :
    '''
    Endpoint that returns suggested events to the user, powered by the Eventful API - http://api.eventful.com/
    '''
    return getSuggestedEvents()

@app.route('/freeTime')
@crossdomain(origin='*')
def FreeTime() :
    '''
    Endpoint that determines the free time for a user based on entries in their Google Calendar.
    '''
    accessToken = request.args.get('token')
    response = freeTime(accessToken)
    return response

@app.route('/calEvents')
@crossdomain(origin='*')
def CalEvents() :
    '''
    Endpoint that returns all events on a users calendar
    '''
    accessToken = request.args.get('token')
    return getCalEvents(accessToken)

@app.route('/addItem', methods=['POST', 'GET'])
@crossdomain(origin='*')
def addItem() :
    '''
    Endpoint that allows the user to add items to a users Calendar
    '''
    if request.method == 'POST' :
        return addNewItem(request.form)

if __name__ == '__main__':
    app.debug = True
    app.run()
