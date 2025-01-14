/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package androidx.media3.transformer.mh;

import static androidx.media3.common.util.Assertions.checkNotNull;
import static com.google.common.truth.Truth.assertWithMessage;

import android.content.Context;
import android.graphics.Matrix;
import androidx.media3.common.MimeTypes;
import androidx.media3.transformer.AndroidTestUtil;
import androidx.media3.transformer.TransformationRequest;
import androidx.media3.transformer.TransformationTestResult;
import androidx.media3.transformer.Transformer;
import androidx.media3.transformer.TransformerAndroidTestRunner;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Tests repeated transcoding operations (as a stress test and to help reproduce flakiness). */
@RunWith(AndroidJUnit4.class)
public final class RepeatedTranscodeTransformationTest {
  private static final int TRANSCODE_COUNT = 10;

  @Test
  public void repeatedTranscode_givesConsistentLengthOutput() throws Exception {
    Context context = ApplicationProvider.getApplicationContext();
    Matrix transformationMatrix = new Matrix();
    transformationMatrix.postTranslate(/* dx= */ 0.1f, /* dy= */ 0.1f);
    Transformer transformer =
        new Transformer.Builder(context)
            .setTransformationRequest(
                new TransformationRequest.Builder()
                    .setTransformationMatrix(transformationMatrix)
                    // Video MIME type is H264.
                    .setAudioMimeType(MimeTypes.AUDIO_AAC)
                    .build())
            .build();

    Set<Long> differentOutputSizesBytes = new HashSet<>();
    for (int i = 0; i < TRANSCODE_COUNT; i++) {
      // Use a long video in case an error occurs a while after the start of the video.
      TransformationTestResult testResult =
          new TransformerAndroidTestRunner.Builder(context, transformer)
              .build()
              .run(
                  /* testId= */ "repeatedTranscode_givesConsistentLengthOutput_" + i,
                  AndroidTestUtil.MP4_REMOTE_H264_MP3_URI_STRING);
      differentOutputSizesBytes.add(checkNotNull(testResult.transformationResult.fileSizeBytes));
    }

    assertWithMessage(
            "Different transcoding output sizes detected. Sizes: " + differentOutputSizesBytes)
        .that(differentOutputSizesBytes.size())
        .isEqualTo(1);
  }

  @Test
  public void repeatedTranscodeNoAudio_givesConsistentLengthOutput() throws Exception {
    Context context = ApplicationProvider.getApplicationContext();
    Matrix transformationMatrix = new Matrix();
    transformationMatrix.postTranslate(/* dx= */ 0.1f, /* dy= */ 0.1f);
    Transformer transformer =
        new Transformer.Builder(context)
            .setRemoveAudio(true)
            .setTransformationRequest(
                new TransformationRequest.Builder()
                    // Video MIME type is H264.
                    .setTransformationMatrix(transformationMatrix)
                    .build())
            .build();

    Set<Long> differentOutputSizesBytes = new HashSet<>();
    for (int i = 0; i < TRANSCODE_COUNT; i++) {
      // Use a long video in case an error occurs a while after the start of the video.
      TransformationTestResult testResult =
          new TransformerAndroidTestRunner.Builder(context, transformer)
              .build()
              .run(
                  /* testId= */ "repeatedTranscodeNoAudio_givesConsistentLengthOutput_" + i,
                  AndroidTestUtil.MP4_REMOTE_H264_MP3_URI_STRING);
      differentOutputSizesBytes.add(checkNotNull(testResult.transformationResult.fileSizeBytes));
    }

    assertWithMessage(
            "Different transcoding output sizes detected. Sizes: " + differentOutputSizesBytes)
        .that(differentOutputSizesBytes.size())
        .isEqualTo(1);
  }

  @Test
  public void repeatedTranscodeNoVideo_givesConsistentLengthOutput() throws Exception {
    Context context = ApplicationProvider.getApplicationContext();
    Transformer transformer =
        new Transformer.Builder(context)
            .setRemoveVideo(true)
            .setTransformationRequest(
                new TransformationRequest.Builder().setAudioMimeType(MimeTypes.AUDIO_AAC).build())
            .build();

    Set<Long> differentOutputSizesBytes = new HashSet<>();
    for (int i = 0; i < TRANSCODE_COUNT; i++) {
      // Use a long video in case an error occurs a while after the start of the video.
      TransformationTestResult testResult =
          new TransformerAndroidTestRunner.Builder(context, transformer)
              .build()
              .run(
                  /* testId= */ "repeatedTranscodeNoVideo_givesConsistentLengthOutput_" + i,
                  AndroidTestUtil.MP4_REMOTE_H264_MP3_URI_STRING);
      differentOutputSizesBytes.add(checkNotNull(testResult.transformationResult.fileSizeBytes));
    }

    assertWithMessage(
            "Different transcoding output sizes detected. Sizes: " + differentOutputSizesBytes)
        .that(differentOutputSizesBytes.size())
        .isEqualTo(1);
  }
}
