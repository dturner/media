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

import static androidx.media3.transformer.AndroidTestUtil.MP4_ASSET_SEF_URI_STRING;
import static androidx.media3.transformer.AndroidTestUtil.MP4_ASSET_URI_STRING;
import static androidx.media3.transformer.AndroidTestUtil.MP4_ASSET_WITH_INCREASING_TIMESTAMPS_URI_STRING;
import static androidx.media3.transformer.AndroidTestUtil.MP4_REMOTE_4K60_PORTRAIT_URI_STRING;

import android.content.Context;
import androidx.media3.common.Format;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.Util;
import androidx.media3.transformer.Codec;
import androidx.media3.transformer.TransformationException;
import androidx.media3.transformer.TransformationRequest;
import androidx.media3.transformer.Transformer;
import androidx.media3.transformer.TransformerAndroidTestRunner;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;

/** {@link Transformer} instrumentation tests. */
@RunWith(AndroidJUnit4.class)
public class TransformationTest {

  private static final String TAG = "TransformationTest";

  @Test
  public void transform() throws Exception {
    final String testId = TAG + "_transform";

    Context context = ApplicationProvider.getApplicationContext();
    Transformer transformer = new Transformer.Builder(context).build();
    // TODO(b/223381524): Enable Ssim calculation after fixing queueInputBuffer exception.
    new TransformerAndroidTestRunner.Builder(context, transformer)
        .build()
        .run(testId, MP4_ASSET_WITH_INCREASING_TIMESTAMPS_URI_STRING);
  }

  @Test
  public void transformWithDecodeEncode() throws Exception {
    final String testId = TAG + "_transformForceCodecUse";

    Context context = ApplicationProvider.getApplicationContext();
    Transformer transformer =
        new Transformer.Builder(context)
            .setEncoderFactory(
                new Codec.EncoderFactory() {
                  @Override
                  public Codec createForAudioEncoding(Format format, List<String> allowedMimeTypes)
                      throws TransformationException {
                    return Codec.EncoderFactory.DEFAULT.createForAudioEncoding(
                        format, allowedMimeTypes);
                  }

                  @Override
                  public Codec createForVideoEncoding(Format format, List<String> allowedMimeTypes)
                      throws TransformationException {
                    return Codec.EncoderFactory.DEFAULT.createForVideoEncoding(
                        format, allowedMimeTypes);
                  }

                  @Override
                  public boolean audioNeedsEncoding() {
                    return true;
                  }

                  @Override
                  public boolean videoNeedsEncoding() {
                    return true;
                  }
                })
            .build();
    // TODO(b/223381524): Enable Ssim calculation after fixing queueInputBuffer exception.
    new TransformerAndroidTestRunner.Builder(context, transformer)
        .build()
        .run(testId, MP4_ASSET_WITH_INCREASING_TIMESTAMPS_URI_STRING);
  }

  @Test
  public void transform4K60() throws Exception {
    final String testId = TAG + "_transform4K60";

    // TODO(b/223381524): Enable Ssim calculation after fixing queueInputBuffer exception.
    Context context = ApplicationProvider.getApplicationContext();
    Transformer transformer = new Transformer.Builder(context).build();
    new TransformerAndroidTestRunner.Builder(context, transformer)
        .build()
        .run(testId, MP4_REMOTE_4K60_PORTRAIT_URI_STRING);
  }

  @Test
  public void transformNoAudio() throws Exception {
    final String testId = TAG + "_transformNoAudio";

    // TODO(b/223381524): Enable Ssim calculation after fixing queueInputBuffer exception.
    Context context = ApplicationProvider.getApplicationContext();
    Transformer transformer = new Transformer.Builder(context).setRemoveAudio(true).build();
    new TransformerAndroidTestRunner.Builder(context, transformer)
        .build()
        .run(testId, MP4_ASSET_WITH_INCREASING_TIMESTAMPS_URI_STRING);
  }

  @Test
  public void transformNoVideo() throws Exception {
    final String testId = TAG + "_transformNoVideo";

    Context context = ApplicationProvider.getApplicationContext();
    Transformer transformer = new Transformer.Builder(context).setRemoveVideo(true).build();
    new TransformerAndroidTestRunner.Builder(context, transformer)
        .build()
        .run(testId, MP4_ASSET_URI_STRING);
  }

  @Test
  public void transformSef() throws Exception {
    final String testId = TAG + "_transformSef";

    if (Util.SDK_INT < 25) {
      // TODO(b/210593256): Remove test skipping after removing the MediaMuxer dependency.
      Log.i(testId, "Skipping on this API version due to lack of muxing support");
      return;
    }

    Context context = ApplicationProvider.getApplicationContext();
    Transformer transformer =
        new Transformer.Builder(context)
            .setTransformationRequest(
                new TransformationRequest.Builder().setFlattenForSlowMotion(true).build())
            .build();
    // TODO(b/223381524): Enable Ssim calculation after fixing queueInputBuffer exception.
    new TransformerAndroidTestRunner.Builder(context, transformer)
        .build()
        .run(testId, MP4_ASSET_SEF_URI_STRING);
  }
}
