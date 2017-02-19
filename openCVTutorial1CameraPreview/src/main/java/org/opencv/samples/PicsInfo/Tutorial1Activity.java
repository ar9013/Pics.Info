package org.opencv.samples.PicsInfo;

import java.io.IOException;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import android.app.Activity;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.opencv.samples.PicsInfo.R;


public class Tutorial1Activity extends Activity implements CvCameraViewListener2 {
	private Tutorial1Activity activity;
    private static final String TAG = "OCVSample::Activity";

	// FlagDraw
	private boolean flagDraw =false;
	private String FLAGDRAW = "FLAGDRAW";

    private CameraBridgeViewBase mOpenCvCameraView;
    private TextView imgTitle, imgDisp,PPTITLE,PPDISP;
	private VerticalTextView imgTitle_p,imgDisp_p;
	private LinearLayout textarea_p ;

    private boolean              mIsJavaCamera = true;
    private MenuItem             mItemSwitchCamera = null;

 // A key for storing the index of the active image size.
 	private static final String STATE_IMAGE_SIZE_INDEX = "imageSizeIndex";

 	// Keys for storing the indices of the active filters.
 	private static final String STATE_IMAGE_DETECTION_FILTER_INDEX = "imageDetectionFilterIndex";

 // The filters.
 	private ImageDetectionFilter[] mImageDetectionFilters;

 	// The indices of the active filters.
 	private int mImageDetectionFilterIndex;

 	// Target found index.
 	private int foundTargetIndex = -1;

	private int falseCount = 0;

 // The index of the active image size.
 	private int mImgSizeIndex;

 	private Camera mCamera;

	private ImageProcessTask imageProcessTask;

	Mat	mRgba,mRgbaF,mRgbaT;

	private static final int MY_PERMISSIONS_REQUEST_OPEN_CAMERA = 1;

	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:


                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();

                    final ImageDetectionFilter chengpo;
    				try {
    					chengpo = new ImageDetectionFilter(Tutorial1Activity.this,
    							R.drawable.chengpo);
    				} catch (IOException e) {
    					Log.e(TAG, "Failed to load drawable: " + "chengpo");
    					e.printStackTrace();
    					break;
    				}

    				final ImageDetectionFilter chiayi;
    				try {
    					chiayi = new ImageDetectionFilter(
    							Tutorial1Activity.this,
    							R.drawable.chiayi);
    				} catch (IOException e) {
    					Log.e(TAG, "Failed to load drawable: "
    							+ "chiayi");
    					e.printStackTrace();
    					break;
    				}

    				final ImageDetectionFilter summer_street;
    				try {
    					summer_street = new ImageDetectionFilter(
    							Tutorial1Activity.this,
    							R.drawable.summer_street);
    				} catch (IOException e) {
    					Log.e(TAG, "Failed to load drawable: "
    							+ "summer_street");
    					e.printStackTrace();
    					break;
    				}

					final ImageDetectionFilter bitan;
					try {
						bitan = new ImageDetectionFilter(
								Tutorial1Activity.this,
								R.drawable.bitan);
					} catch (IOException e) {
						Log.e(TAG, "Failed to load drawable: "
								+ "summer_street");
						e.printStackTrace();
						break;
					}


					final ImageDetectionFilter bridge;
					try {
						bridge = new ImageDetectionFilter(
								Tutorial1Activity.this,
								R.drawable.bridge);
					} catch (IOException e) {
						Log.e(TAG, "Failed to load drawable: "
								+ "summer_street");
						e.printStackTrace();
						break;
					}

					final ImageDetectionFilter chiayi_park;
					try {
						chiayi_park = new ImageDetectionFilter(
								Tutorial1Activity.this,
								R.drawable.chiayi_park);
					} catch (IOException e) {
						Log.e(TAG, "Failed to load drawable: "
								+ "summer_street");
						e.printStackTrace();
						break;
					}

					final ImageDetectionFilter clean;
					try {
						clean = new ImageDetectionFilter(
								Tutorial1Activity.this,
								R.drawable.clean);
					} catch (IOException e) {
						Log.e(TAG, "Failed to load drawable: "
								+ "summer_street");
						e.printStackTrace();
						break;
					}

					final ImageDetectionFilter family;
					try {
						family = new ImageDetectionFilter(
								Tutorial1Activity.this,
								R.drawable.family);
					} catch (IOException e) {
						Log.e(TAG, "Failed to load drawable: "
								+ "summer_street");
						e.printStackTrace();
						break;
					}

					final ImageDetectionFilter school;
					try {
						school = new ImageDetectionFilter(
								Tutorial1Activity.this,
								R.drawable.school);
					} catch (IOException e) {
						Log.e(TAG, "Failed to load drawable: "
								+ "summer_street");
						e.printStackTrace();
						break;
					}

					final ImageDetectionFilter shanghai;
					try {
						shanghai = new ImageDetectionFilter(
								Tutorial1Activity.this,
								R.drawable.shanghai);
					} catch (IOException e) {
						Log.e(TAG, "Failed to load drawable: "
								+ "summer_street");
						e.printStackTrace();
						break;
					}

					final ImageDetectionFilter water_pool;
					try {
						water_pool = new ImageDetectionFilter(
								Tutorial1Activity.this,
								R.drawable.water_pool);
					} catch (IOException e) {
						Log.e(TAG, "Failed to load drawable: "
								+ "summer_street");
						e.printStackTrace();
						break;
					}

    				mImageDetectionFilters = new ImageDetectionFilter[] {summer_street, chengpo, chiayi,
							water_pool,shanghai,school,family,clean,chiayi_park,bridge,bitan };

                 break;


                default:

                    super.onManagerConnected(status);
                break;
            }
        }
    };

    public Tutorial1Activity() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        if (savedInstanceState != null) {

			mImageDetectionFilterIndex = savedInstanceState.getInt(
					STATE_IMAGE_DETECTION_FILTER_INDEX, 0);
			mImgSizeIndex = savedInstanceState
					.getInt(STATE_IMAGE_SIZE_INDEX, 0);

		} else {

			mImgSizeIndex = 0;
			mImageDetectionFilterIndex = 0;

		}

        setContentView(R.layout.tutorial1_surface_view);

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_java_surface_view);

		//textarea_p = (LinearLayout) findViewById(R.id.textarea_p);


		imgTitle = (TextView) findViewById(R.id.imgTitle);
        imgDisp = (TextView) findViewById(R.id.imgDisp);

		imgDisp_p = (VerticalTextView) findViewById(R.id.vImgDisp);
		imgTitle_p = (VerticalTextView) findViewById(R.id.vImgTitle);

// 檢查權限
		if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_OPEN_CAMERA);
		} else {
			mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
			mOpenCvCameraView.setMaxFrameSize(1280, 720);


			mOpenCvCameraView.setCvCameraViewListener(this);
		}
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }


//    public void onCameraViewStarted(int width, int height) {
//        android.hardware.Camera.Size res = mOpenCvCameraView.getResolutionList().get(((Tutorial1Activity) mOpenCvCameraView).getResolutionList().size()-1);
//        mOpenCvCameraView.setResolution(res);
//    }
//    
//    public List<Size> getResolutionList() {
//        return mCamera.getParameters().getSupportedPreviewSizes();
//    }

    public void onCameraViewStopped() {
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
    	final Mat rgba = inputFrame.rgba();


		if (imageProcessTask != null && !imageProcessTask.isCancelled()) {
			imageProcessTask.cancel(true);
			imageProcessTask = null;
        }

        if(imageProcessTask == null) {
			imageProcessTask = new ImageProcessTask();
			imageProcessTask.execute(rgba); // 傳入 rgba
        }


		//Core.flip(rgba, rgba, 1);
			// Apply the active filters.

//   for(mImageDetectionFilterIndex = 0 ; mImageDetectionFilterIndex < mImageDetectionFilters.length;mImageDetectionFilterIndex++){
//
//    		if(mImageDetectionFilterIndex == mImageDetectionFilters.length){
//    			mImageDetectionFilterIndex = 0;
//   		}
//
//
//
//    		mImageDetectionFilters[mImageDetectionFilterIndex].apply(rgba, rgba);
//        	flagDraw = mImageDetectionFilters[mImageDetectionFilterIndex].targetFound();
//
//        	Log.d(FLAGDRAW, "flagDraw : "+ mImageDetectionFilterIndex);
//        	Log.d(FLAGDRAW, "flagDraw : "+ flagDraw);
//
//
//        		if(flagDraw){
//
//        			foundTargetIndex = mImageDetectionFilterIndex;
//        			//mImageDetectionFilters[foundTargetIndex].apply(rgba, rgba);
//            		//flagDraw = mImageDetectionFilters[mImageDetectionFilterIndex].targetFound();
//
//            		Log.d(FLAGDRAW, "!!!!flagDraw : "+ foundTargetIndex);
//            		Log.d(FLAGDRAW, "!!!!flagDraw : "+ flagDraw);
//
//            		switch (foundTargetIndex) {
//            		case 2:
//						// 設定文字說明
//						Thread chiayi = new Thread(new Runnable() {
//
//							@Override
//							public void run() {
//
//								mHandler.sendEmptyMessage(2);
//
//							}
//
//						});
//						chiayi.start();
//
//						break;
//
//
//					case 1:
//						// 設定文字說明
//						Thread chengpo = new Thread(new Runnable() {
//
//							@Override
//							public void run() {
//
//								mHandler.sendEmptyMessage(1);
//
//							}
//
//						});
//						chengpo.start();
//
//						break;
//
//					case 0:
//						// 設定文字說明
//						Thread summer_street = new Thread(new Runnable() {
//
//							@Override
//							public void run() {
//
//								mHandler.sendEmptyMessage(0);
//
//							}
//
//						});
//						summer_street.start();
//						break;
//
//					}
//
//
//        		}
//
//    		}
		return rgba;
    }

	// ６．０ 請求權限
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

		if (requestCode == MY_PERMISSIONS_REQUEST_OPEN_CAMERA) {
			if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
				mOpenCvCameraView.setMaxFrameSize(1280, 720);


				mOpenCvCameraView.setCvCameraViewListener(this);
			} else {
				Toast.makeText(Tutorial1Activity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
			}
			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}

	}


Boolean isVertical = false;
	int iVertical = 0;


		Handler mHandler = new Handler() {


        @Override
        public void handleMessage(Message msg) {

			if(msg.what == 10) {
			//	char[] title = getResources().getString(R.string.bitan_title).toCharArray();

//				for(char c :title){
//					imgTitle.setText(c);
//				}

				imgTitle.setText(R.string.bitan_title);
				imgDisp.setText(R.string.bitan_des);
			//	imgTitle.setAnimation(AnimationUtils.loadAnimation(Tutorial1Activity.this,android.R.anim.slide_in_left));
			//	imgDisp.setAnimation(AnimationUtils.loadAnimation(Tutorial1Activity.this,android.R.anim.slide_in_left));

				imgTitle_p.setText("");
				imgDisp_p.setText("");
			}

			if(msg.what == 9) {
				imgTitle.setText(R.string.bridge_title);
				imgDisp.setText(R.string.bridge_des);
				imgTitle_p.setText("");
				imgDisp_p.setText("");
			}

			if(msg.what == 8) {
				imgTitle.setText(R.string.chiayi_park_title);
				imgDisp.setText(R.string.chiayi_park_des);
				imgTitle_p.setText("");
				imgDisp_p.setText("");
			}
		// 直版
			if(msg.what == 7) {
				imgTitle.setText("");
				imgDisp.setText("");
				imgTitle_p.setText(R.string.clean_title);
				imgDisp_p.setText(R.string.clean_des);
			}

			if(msg.what == 6) {
				imgTitle.setText(R.string.family_title);
				imgDisp.setText(R.string.family_des);
				imgTitle_p.setText("");
				imgDisp_p.setText("");
			}

			if(msg.what == 5) {
				imgTitle.setText(R.string.school_title);
				imgDisp.setText(R.string.school_des);
				imgTitle_p.setText("");
				imgDisp_p.setText("");
			}

			if(msg.what == 4) {
				imgTitle.setText(R.string.shanghai_title);
				imgDisp.setText(R.string.shanghai_des);
				imgTitle_p.setText("");
				imgDisp_p.setText("");
			}

			if(msg.what == 3) {
				imgTitle.setText(R.string.water_pool_title);
				imgDisp.setText(R.string.water_pool_des);
				imgTitle_p.setText("");
				imgDisp_p.setText("");
			}

			// 直式
        	 if(msg.what == 2) {
				 imgTitle.setText("");
				 imgDisp.setText("");
				 imgTitle_p.setText(R.string.chiayi_title);
				 imgDisp_p.setText(R.string.chiayi_des);
			 }

            if(msg.what == 1) {
				imgTitle.setText(R.string.chengpo_title);
            	imgDisp.setText(R.string.chengpo_des);
				imgTitle_p.setText("");
				imgDisp_p.setText("");
            }

            if(msg.what == 0){
				imgTitle.setText(R.string.summer_street_title);
            	imgDisp.setText(R.string.summer_street_des);
				imgTitle_p.setText("");
				imgDisp_p.setText("");
            }

            // 找不到
			if(msg.what == -1){
				imgTitle.setText("");
				imgDisp.setText("");
				imgTitle_p.setText("");
				imgDisp_p.setText("");
			}
            super.handleMessage(msg);
        }
    };

	@Override
	public void onCameraViewStarted(int width, int height) {
		// TODO Auto-generated method stub



		mRgbaT = new Mat(width, height, CvType.CV_8UC4);  // NOTE width,width is NOT a typo

	}




	private class ImageProcessTask extends AsyncTask<Mat,Mat,Mat>{

		private Mat bgMat;

		@Override
		protected Mat doInBackground(Mat... mats) {

			// 收到傳入的 Mat
			bgMat = mats[0];


			// 重複找
			for(mImageDetectionFilterIndex = 0 ; mImageDetectionFilterIndex < mImageDetectionFilters.length;mImageDetectionFilterIndex++) {

				if (mImageDetectionFilterIndex == mImageDetectionFilters.length) {
					mImageDetectionFilterIndex = 0;
				}

				mImageDetectionFilters[mImageDetectionFilterIndex].apply(bgMat, bgMat);
				flagDraw = mImageDetectionFilters[mImageDetectionFilterIndex].targetFound();

				Log.d(FLAGDRAW, "flagDraw : "+ mImageDetectionFilterIndex);
				Log.d(FLAGDRAW, "flagDraw : "+ flagDraw);

				// 找到的話就做下面的動作
				if(flagDraw){
					falseCount = 0;
        			foundTargetIndex = mImageDetectionFilterIndex;
        			//mImageDetectionFilters[foundTargetIndex].apply(rgba, rgba);
            		//flagDraw = mImageDetectionFilters[mImageDetectionFilterIndex].targetFound();

            		Log.d(FLAGDRAW, "!!!!flagDraw : "+ foundTargetIndex);
            		Log.d(FLAGDRAW, "!!!!flagDraw : "+ flagDraw);

            		switch (foundTargetIndex) {
						case 10:
							// 設定文字說明
							Thread bitan = new Thread(new Runnable() {

								@Override
								public void run() {

									mHandler.sendEmptyMessage(10);

								}

							});
							bitan.start();

							break;

						case 9:
							// 設定文字說明
							Thread bridge = new Thread(new Runnable() {

								@Override
								public void run() {

									mHandler.sendEmptyMessage(9);

								}

							});
							bridge.start();

							break;

						case 8:
							// 設定文字說明
							Thread chiayi_park = new Thread(new Runnable() {

								@Override
								public void run() {

									mHandler.sendEmptyMessage(8);

								}

							});
							chiayi_park.start();

							break;

						case 7:
							// 設定文字說明
							Thread clean = new Thread(new Runnable() {

								@Override
								public void run() {

									mHandler.sendEmptyMessage(7);

								}

							});
							clean.start();

							break;

						case 6:
							// 設定文字說明
							Thread family = new Thread(new Runnable() {

								@Override
								public void run() {

									mHandler.sendEmptyMessage(6);

								}

							});
							family.start();

							break;

						case 4:
							// 設定文字說明
							Thread shanghai = new Thread(new Runnable() {

								@Override
								public void run() {

									mHandler.sendEmptyMessage(4);

								}

							});
							shanghai.start();

							break;

						case 3:
							// 設定文字說明
							Thread water_pool = new Thread(new Runnable() {

								@Override
								public void run() {

									mHandler.sendEmptyMessage(3);

								}

							});
							water_pool.start();

							break;

						case 5:
							// 設定文字說明
							Thread school = new Thread(new Runnable() {

								@Override
								public void run() {

									mHandler.sendEmptyMessage(5);

								}

							});
							school.start();

							break;

            		case 2:
						// 設定文字說明
						Thread chiayi = new Thread(new Runnable() {

							@Override
							public void run() {

								mHandler.sendEmptyMessage(2);

							}

						});
						chiayi.start();

						break;


					case 1:
						// 設定文字說明
						Thread chengpo = new Thread(new Runnable() {

							@Override
							public void run() {

								mHandler.sendEmptyMessage(1);

							}

						});
						chengpo.start();

						break;

					case 0:
						// 設定文字說明
						Thread summer_street = new Thread(new Runnable() {

							@Override
							public void run() {

								mHandler.sendEmptyMessage(0);

							}

						});
						summer_street.start();
						break;

					}


				}else {
					falseCount = falseCount + 1;
					if (falseCount > 15) { // 當圖片增加後，記得要調整比圖片還多
						Thread notfound = new Thread(new Runnable() {
							@Override
							public void run() {
								mHandler.sendEmptyMessage(-1);
							}
						});
						notfound.start();
					}
				}
			}

			return null;
		}
	}





}
