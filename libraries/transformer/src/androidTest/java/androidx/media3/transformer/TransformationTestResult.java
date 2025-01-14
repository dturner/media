/*
 * Copyright 2022 The Android Open Source Project
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
package androidx.media3.transformer;

/** A test only class for holding the details of a test transformation. */
public class TransformationTestResult {
  /** Represents an unset or unknown SSIM score. */
  public static final double SSIM_UNSET = -1.0d;

  public final TransformationResult transformationResult;
  public final String filePath;
  /** The amount of time taken to perform the transformation in milliseconds. */
  public final long transformationDurationMs;
  /** The SSIM score of the transformation, {@link #SSIM_UNSET} if unavailable. */
  public final double ssim;

  public TransformationTestResult(
      TransformationResult transformationResult, String filePath, long transformationDurationMs) {
    this(transformationResult, filePath, transformationDurationMs, /* ssim= */ SSIM_UNSET);
  }

  public TransformationTestResult(
      TransformationResult transformationResult,
      String filePath,
      long transformationDurationMs,
      double ssim) {
    this.transformationResult = transformationResult;
    this.filePath = filePath;
    this.transformationDurationMs = transformationDurationMs;
    this.ssim = ssim;
  }
}
