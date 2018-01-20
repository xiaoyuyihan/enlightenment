#include <jni.h>
#include <string>

extern "C"
jstring
Java_enlightenment_com_enlightenment_LoginActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
