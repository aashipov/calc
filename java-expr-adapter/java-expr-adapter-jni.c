#include "c-exprtk-adapter.h"
#include "c-meval-adapter.h"
#include "org_dummy_calc_JavaExprAdapterJni.h"
#include <jni.h>

JNIEXPORT jdouble JNICALL
Java_org_dummy_calc_JavaExprAdapterJni_calculateViaExprtkJni(JNIEnv *env,
                                                             jclass jclass,
                                                             jstring jstring) {
  jboolean isCopy;
  const char *expression = (*env)->GetStringUTFChars(env, jstring, &isCopy);
  double result = calculate_via_exprtk(expression);
  (*env)->ReleaseStringUTFChars(env, jstring, expression);
  return result;
}

JNIEXPORT jdouble JNICALL
Java_org_dummy_calc_JavaExprAdapterJni_calculateViaMevalJni(JNIEnv *env,
                                                            jclass jclass,
                                                            jstring jstring) {
  jboolean isCopy;
  const char *expression = (*env)->GetStringUTFChars(env, jstring, &isCopy);
  double result = calculate_via_meval(expression);
  (*env)->ReleaseStringUTFChars(env, jstring, expression);
  return result;
}
