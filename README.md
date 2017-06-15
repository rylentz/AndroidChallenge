The app requirements:
1. Login using either Facebook
2. A screen showing the trending hashtags after user logs in
3. List of influencers when a hashtag is selected

The API documentation can be found here: http://docs.ritekit.apiary.io

The minimum Android version that this application was tested on is 6.0.1.

Logging in with Facebook was achieved by reviewing the Facebook documentation and various tutorials to find the simplest method.
Activities making use of ListView, ArrayList, and ArrayAdapter were implemented to display the hashtags and influencers.
A Utils class and an extension of AsyncTaskLoader were created to deal with downloading the data from the API.
