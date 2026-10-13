#include "c-exprtk-adapter.h"
#include "org_dummy_calc_JavaExprAdapter.h"
#include "rs-expr-adapter.h"
#include <jni.h>

JNIEXPORT jdouble JNICALL
Java_org_dummy_calc_JavaExprAdapter_calculateViaExprtkJni(JNIEnv *env,
                                                          jclass jclass,
                                                          jstring jstring) {
  jboolean isCopy;
  const char *expression = (*env)->GetStringUTFChars(env, jstring, &isCopy);
  double result = calculate_via_exprtk(expression);
  (*env)->ReleaseStringUTFChars(env, jstring, expression);
  return result;
}

JNIEXPORT jdouble JNICALL
Java_org_dummy_calc_JavaExprAdapter_calculateViaMevalJni(JNIEnv *env,
                                                         jclass jclass,
                                                         jstring jstring) {
  jboolean isCopy;
  const char *expression = (*env)->GetStringUTFChars(env, jstring, &isCopy);
  double result = calculate_via_meval(expression);
  (*env)->ReleaseStringUTFChars(env, jstring, expression);
  return result;
}
