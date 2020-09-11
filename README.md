<p align="center">
  <img src="readme_res/nut.jpg" width="400">
<p>Nut (Ancient Egyptian: Nwt), also known by various other transcriptions,
is the goddess of the sky, stars, cosmos, mothers, astronomy, and the universe in the ancient Egyptian religion.
She was seen as a star-covered woman arching over the Earth.
</p>
</p>

<h1 align="center">
Nut <br/>
A clean quick to use RXJava, Retrofit and View model</h2>

MIT License

Copyright (c) 2020 Abanoub Milad Nassief Hanna\
abanoubcs@gmail.com\
[@Linkedin](https://www.linkedin.com/in/abanoubmilad/)\
[@Github](https://github.com/abanoubmilad)


- implemented in MVVM
- supports RxJava
- easily handles network calls, network failure or model parsing errors
- easily fire parallel network calls
- clean structure that avoids view model leakage
- open source
- available on jitpack
- easy to integrate and extend

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

        implementation 'com.github.abanoubmilad:nut:0.3'
        
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

            }, {

                AppLogger.e("NetworkError", "first request" + it.code.toString())
                AppLogger.e(
                    "NetworkError",
                    it.parseAs<ErrorResponse>()?.error?.message.orEmpty()
                )

            }, bookRepository.searchVolumes(keyword, author), {
                volumesResponseLiveData.postValue(it)

            }, {
                AppLogger.e("NetworkError", "second request" + it.code.toString())
                AppLogger.e(
                    "NetworkError",
                    it.parseAs<ErrorResponse>()?.error?.message.orEmpty()
                )

            },
            bookRepository.searchVolumes(keyword, author), {
                volumesResponseLiveData.postValue(it)

            }, {
                AppLogger.e("NetworkError", "third request" + it.code.toString())
                AppLogger.e(
                    "NetworkError",
                    it.parseAs<ErrorResponse>()?.error?.message.orEmpty()
                )

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