from flask import Flask, request, render_template, send_from_directory

app = Flask(__name__)
events = []
users = []

# Handling for requests coming through "http://<hostname>:8000"
@app.route('/', methods=['GET','POST'])
def home():
    # This function displays the index page for this web app. 
    #   This feature is mostly used for testing purposes only.
    if (request.method == 'POST'):
        events.append({'venue':request.form['venue'], 'time':request.form['time'], 'category':request.form['category'], 'numPeople':request.form['numPeople']})
        #if len(events) > 10:
            #del events[0]
    return render_template("index.html", target="/", events_list=events)


# Handling for requests coming through "http://<hostname>:8000/get_messages"
@app.route('/get_events')
def get_events():
    # This function responds with the list of messages currently stored in
    #   the chat server. The response format is as follows:
    #
    #   <user>|<message>;<user>|<message>;...
    #
    event_str = ""

    evt_list_len = len(events)
    for i in range(0, evt_list_len):
        event_str += events[i]['index'] + "|" + events[i]['venue'] + "|" + events[i]['time'] + "|" + events[i]['category'] + "|" + events[i]['numPeople'] + "|" + events[i]['user'] + ";"

    return event_str

# Handling for requests coming through "http://<hostname>:8000/get_messages"
@app.route('/get_users')
def get_users():
    # This function responds with the list of messages currently stored in
    #   the chat server. The response format is as follows:
    #
    #   <user>|<message>;<user>|<message>;...
    #
    user_str = ""

    user_list_len = len(users)
    for i in range(0, user_list_len):
        user_str += users[i]['username'] + "|" + users[i]['fullname'] + "|" + users[i]['company'] + "|" + users[i]['contact'] + "|" + users[i]['password'] + ";"

    return user_str

# Handling for requests coming through "http://<hostname>:8000/get_messages"
@app.route('/upload_events', methods=['POST'])
def upload_events():
    # This function accepts POST requests containing a 'usr' and 'msg' param
    #   and stores them as new messages in the chat server. 
    #
    #   Currently, the limit for number of messages stored is 10 as we're not 
    #   using any database here (only server memory). This can easily be 
    #   tweaked without any problems.
    #
    if (request.method == 'POST'):
        index = request.form['index']
        if index == "":
            return "Failed!"

        venue = request.form['venue']
        if venue == "":
            return "Failed!"

        time = request.form['time']
        if time == "":
            return "Failed!"

        category = request.form['category']
        if category == "":
            return "Failed!"

        numPeople = request.form['numPeople']
        if numPeople == "":
            return "Failed!"

        user = request.form['user']
        if user == "":
            return "Failed!"
        events.append({ 'index':index, 'venue':venue, 'time':time, 'category':category, 'numPeople':numPeople, 'user':user })
    return "Failed!"

# Handling for requests coming through "http://<hostname>:8000/get_messages"
@app.route('/upload_users', methods=['POST'])
def upload_users():
    # This function accepts POST requests containing a 'usr' and 'msg' param
    #   and stores them as new messages in the chat server.
    #
    #   Currently, the limit for number of messages stored is 10 as we're not
    #   using any database here (only server memory). This can easily be
    #   tweaked without any problems.
    #
    if (request.method == 'POST'):
        username = request.form['username']
        if username == "":
            return "Failed!"

        fullname = request.form['fullname']
        if fullname == "":
            return "Failed!"

        company = request.form['company']
        if company == "":
            return "Failed!"

        contact = request.form['contact']
        if contact == "":
            return "Failed!"

        password = request.form['password']
        if password == "":
            return "Failed!"
        users.append({ 'username':username, 'fullname':fullname, 'company':company, 'contact':contact, 'password':password })
    return "Failed!"

# Handling for requests coming through "http://<hostname>:8000/get_legacy"
@app.route('/get_legacy')
def get_legacy():
    # Not used
    return send_from_directory('files', 'org.apache.http.legacy.jar')


# The following lines are required by Flask to properly run the web app for
#   the simple chat server. Note that this runs on port 8000.
if __name__ == "__main__":
    app.debug = True
    app.run(host='0.0.0.0', port=8000)