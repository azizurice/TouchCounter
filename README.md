## App that counts touches and updates home widget

In this app, we will see how we can count touches per second. The touches per second is calculated based on how many
touches happen in last one second. It also shows how to add a feature called home widget and updates this widget
periodically (especially every 100ms - which is not a good idea in terms of battery drainage). However, for testing
purpose, I have implemented it here. In addition, using OAuth2 protocol, a login feature is added where the backend service is supported by
[Firebase](https://www.firebase.com/) and Twitter has been used as one service provider.



### Goal
To develop this app, I have used the following components:
    1. Activity
    2. IntentService
    3. AppWidgetProvider
    4. Firebase
    5. Google play services
    6. Map Data structure with fixed size : [LinkedHashMap](https://docs.oracle.com/javase/7/docs/api/java/util/LinkedHashMap.html)


### Flow diagram of the app

<img src="https://github.com/azizurice/TouchCounter/blob/master/docs/SchematicDiagram.png" />

### Project Structure in Android Studio

<img src="https://github.com/azizurice/TouchCounter/blob/master/docs/ProjectStructure.png" />


### Few Screen Shots
Login Page:


<img src="https://github.com/azizurice/TouchCounter/blob/master/docs/LoginPage.png" />

TouchCounterActivity:


<img src="https://github.com/azizurice/TouchCounter/blob/master/docs/MainActivity.png" width="800px" height="600px" />

Home Widget:


<img src="https://github.com/azizurice/TouchCounter/blob/master/docs/HomeWidget.png" width="800px" height="600px" />




- Project Structure
