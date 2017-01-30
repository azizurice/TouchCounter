## App that counts touches and updates home widget

In this app, we will see how we can count touches per second. The touches per second is calculated based on how many
touches happen in last one second. It also shows how to add a feature called home widget and updates this widget
periodically (especially every 100ms - which is not a good idea in terms of battery drainage). However, for testing
purpose, I have implemented it here. In addition, using OAuth2 protocol, a login feature is added where the backend service is supported by
[Firebase](https://www.firebase.com/) and Twitter has been used as one service provider.



### Goal
Suppose, we want to show a list of Movies and their brief wiki details from in one of our apps. We have different kinds of android devices( different screen sizes). How can we arrange the GUIs. We can solve this problem using Fragment. How? We can use two fragments: one for displaying movie names and one for displaying their details. In Android Phone, we will inflate(insert) MoviesFragment first, and when user clicks on any particular movie name, it will show the details fragment (WikiDetailsFragment). However, for large screen sizes devices or phone in landscape mode, we can display both fragments together, otherwise most of the spaces will remain blank(which is not good from user experience perspective).


Using this app, we have achieved this goal, please clone the project, import it in Android Studio and play with it. Hope, it will help you to know the advantages of fragments.


See the results and project.

Output and Project Structure
============================

- When our device is phone and orientation is portrait

<img src="https://github.com/azizurice/DroiderNeeds/blob/master/AppSeven/doc/images/Portrait.png" />

- When our device is tablet or phone but orientation is landscape

<img src="https://github.com/azizurice/DroiderNeeds/blob/master/AppSeven/doc/images/Ladscape.png" />

- Project Structure

<img src="https://github.com/azizurice/DroiderNeeds/blob/master/AppSeven/doc/images/ProjectStructure.png" width="800px" height="600px" />