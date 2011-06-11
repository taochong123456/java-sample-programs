/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class main_unittests_utilitys_jni_Console */

#ifndef _Included_main_unittests_utilitys_jni_Console
#define _Included_main_unittests_utilitys_jni_Console
#ifdef __cplusplus
extern "C" {
#endif
#undef main_unittests_utilitys_jni_Console_FOREGROUND_BLACK
#define main_unittests_utilitys_jni_Console_FOREGROUND_BLACK 0L
#undef main_unittests_utilitys_jni_Console_FOREGROUND_BLUE
#define main_unittests_utilitys_jni_Console_FOREGROUND_BLUE 1L
#undef main_unittests_utilitys_jni_Console_FOREGROUND_GREEN
#define main_unittests_utilitys_jni_Console_FOREGROUND_GREEN 2L
#undef main_unittests_utilitys_jni_Console_FOREGROUND_RED
#define main_unittests_utilitys_jni_Console_FOREGROUND_RED 4L
#undef main_unittests_utilitys_jni_Console_FOREGROUND_WHITE
#define main_unittests_utilitys_jni_Console_FOREGROUND_WHITE 7L
#undef main_unittests_utilitys_jni_Console_FOREGROUND_INTENSITY
#define main_unittests_utilitys_jni_Console_FOREGROUND_INTENSITY 8L
#undef main_unittests_utilitys_jni_Console_BACKGROUND_BLUE
#define main_unittests_utilitys_jni_Console_BACKGROUND_BLUE 16L
#undef main_unittests_utilitys_jni_Console_BACKGROUND_GREEN
#define main_unittests_utilitys_jni_Console_BACKGROUND_GREEN 32L
#undef main_unittests_utilitys_jni_Console_BACKGROUND_RED
#define main_unittests_utilitys_jni_Console_BACKGROUND_RED 64L
#undef main_unittests_utilitys_jni_Console_BACKGROUND_INTENSITY
#define main_unittests_utilitys_jni_Console_BACKGROUND_INTENSITY 128L
/*
 * Class:     main_unittests_utilitys_jni_Console
 * Method:    cls
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_main_unittests_utilitys_jni_Console_cls
  (JNIEnv *, jobject);

/*
 * Class:     main_unittests_utilitys_jni_Console
 * Method:    getCurrentColors
 * Signature: ()S
 */
JNIEXPORT jshort JNICALL Java_main_unittests_utilitys_jni_Console_getCurrentColors
  (JNIEnv *, jobject);

/*
 * Class:     main_unittests_utilitys_jni_Console
 * Method:    setCurrentColors
 * Signature: (S)V
 */
JNIEXPORT void JNICALL Java_main_unittests_utilitys_jni_Console_setCurrentColors
  (JNIEnv *, jobject, jshort);

#ifdef __cplusplus
}
#endif
#endif