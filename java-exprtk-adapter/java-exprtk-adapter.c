#include <jni.h>
#include "c-exprtk-adapter.h"
#include "org_dummy_calc_ExprtkAdapter.h"

JNIEXPORT jdouble JNICALL Java_org_dummy_calc_ExprtkAdapter_calculate(
    JNIEnv *env, jclass jclass, jstring jstring) {
        jboolean isCopy;
  const char *expression = (*env)->GetStringUTFChars(env, jstring, &isCopy);
  double result = calculate(expression);
  (*env)->ReleaseStringUTFChars(env, jstring, expression);
  return result;
}
