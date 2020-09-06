<p align="center">
  <img src="readme_res/nut.jpg" width="600">
</p>

<h1 align="center">
Nut 
</h1>

<h2 align="center">
A clean quick to use RXJava, Retrofit and View model</h2>

MIT License

Copyright (c) 2020 Abanoub Milad Nassief Hanna\
abanoubcs@gmail.com\
[@Linkedin](https://www.linkedin.com/in/abanoubmilad/)\
[@Github](https://github.com/abanoubmilad)


## How to add

Add to project level `build.gradle`

```Groovy

allprojects {
    repositories {

        maven { url "https://jitpack.io" }
        
    }
}
```

Add to app level `build.gradle`

```Groovy
    dependencies {

        implementation 'com.github.abanoubmilad:nut:0.1'
        
    }
```

## How to use

```kotlin

   /*
    *
    *   make your view model extends the BaseViewModel
    *
    */

class BookSearchViewModel : BaseViewModel {

   /*
    *
    *   use makeNetworkRequest to consume a single provided by the retrofit interface
    *   network call are handled by background threads
    *   callbacks run on UI thread
    *
    *
    */

    fun searchVolumes(keyword: String?, author: String?) {
        makeNetworkRequest(bookRepository.searchVolumes(keyword, author), {
            volumesResponseLiveData.postValue(it)

        }, {
            AppLogger.e("NetworkError", it.code.toString())
            AppLogger.e(
                "NetworkError",
                it.parseAs<ErrorResponse>()?.error?.message.orEmpty()
            )
        })
    }

   /*
    *
    *   use makeNetworkRequest providing three singles to consume each single provided by the retrofit interface
    *   in parallel
    *   network call are handled by background threads
    *   callbacks run on UI thread
    *
    *
    */

    fun searchVolumesThreeParallel(
        keyword: String?,
        author: String?
    ) {

        makeNetworkRequestsParallel(
            bookRepository.searchVolumes(keyword, author), {
                volumesResponseLiveData.postValue(it)

            }, bookRepository.searchVolumes(keyword, author), {
                volumesResponseLiveData.postValue(it)

            },
            bookRepository.searchVolumes(keyword, author), {
                volumesResponseLiveData.postValue(it)

            }, {
                AppLogger.e("NetworkError", it.code.toString())
                AppLogger.e(
                    "NetworkError",
                    it.parseAs<ErrorResponse>()?.error?.message.orEmpty()
                )
            })

    }
```


```kotlin

   /*
    *
    *   you can use Isync inside your Application class, service or work manager
    *   simply implement the Isync interface
    *   you need to provide the CompositeDisposable
    *   and calling disposeIsync() when your componenet's onDestroy() is called
    *
    */

@AndroidEntryPoint
class FirebaseMsgService : FirebaseMessagingService(), Isync{
    @Inject
    lateinit var authRepo: AuthRepo

    override val disposable = CompositeDisposable()

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        makeNetworkRequest(authRepo.updateNotificationToken(token), {
          }, {
          })

    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
    }


    override fun onDestroy() {
        super.onDestroy()
        disposeIsync()
    }

}

}

```


Copyright 2020 Abanoub Milad Nassief Hanna - abanoubcs@gmail.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.