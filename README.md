# How to Add FirebaseFirestoreLibrary to your App

To use Firebase Firestore Library, first you've to attach Firebase to your app and add required firebase libraries
i.e. (FirebaseAuth,FirebaseFirestore, Firebase Storage)

Add below line to your build.gradle (project level)

maven { url 'https://jitpack.io' }

Add below dependency to your build.gradle (Module App Level)

implementation 'com.github.JalalJanKhan3336:FirebaseFirestoreLibrary:1.0'


# How to Use

Create a Reference of Database Class and specify a type (Model class/Pojo Class Name) in the angular brackets.

Now, call required function i.e. update(), fetchSingle(), fetchAll()


That's it. :) Enjoy!


