
# Hello World, Android (rake)

A small sample Hello World App for use with Android SDK 1.5 + Rake. This is a
quick&dirty proof-of-concept hack to build android projects using Rake instead
of ant. You need to have the various command line build tools (aapt, javac, ...) 
in your PATH.

    $ emulator -avd 1.5 &
    $ rake install ANDROID_SDK=</path/to/sdk>
