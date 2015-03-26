package com.benpaoba.freerun;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.tauth.Tencent;

public class LoginAndProfileInfo extends Activity {
	private final String TAG = "FreeRun";
	
	private boolean logState = false;
	
	private AlertDialog alert;
	private Tencent mTencent;
	//the simple User Info
	private LinearLayout userSummaryInfo;
	private ImageView  userIcon;
	private LinearLayout login;
	private LinearLayout logout;
	private TextView userNickname;
	private TextView editInfo;
	private TextView totalDistance;
	private TextView totalTime;
	private TextView totalCalories;
	
	// total info item for preference
	 static final String NICKNAME = "nickname";
	 static final String TOTALDISTANCE = "total_distance";
	 static final String TOTALTIME = "total_time";
	 static final String TOTALCALORIES = "total_calories";
	 
	 
	
	//The best Record
	private	TextView fastestSpeedMatch;
	private	TextView longestDistance;
	private	TextView longestTime;
	private	TextView shortestTimeFive;
	private TextView shortestTimeTen;
	private TextView shortestTimeHalfMarathon;
	private TextView shortestTimeFullMarathon;
	
	//the Run History Record
	private RelativeLayout checkHistoryRecord;
	private TextView historyTimes;
	
	static final String HISTORYTIMES = "run_history_times";
	//More setup Choice
	private RelativeLayout moreSetup;
	
	//The best Record Item name, Used in Preference
	private final String SPEEDMATCH = "speed_match";
	private final String LONGESTDISTANCE = "longestDistance";
	private final String LONGESTTIME = "longestTime";
	private final String SHORTESTTIME_5 = "shortestTimefive";
	private final String SHORTESTTIME_10 = "shortestTimeTen";
	private final String SHORTESTTIME_HM = "shortestTimeHalfMarathon";
	private final String SHORTESTTIME_FM = "shortestTimeFullMarathon";
	
	public final String PROFILE_INFO_PREFERENCES = "user_profile_info_preference"; 
	
	private SharedPreferences bestHistoryRecordPreference;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_profile_info);
		
		
		// Tencent����SDK����Ҫʵ���࣬�����߿�ͨ��Tencent�������Ѷ���ŵ�OpenAPI��
		// ����APP_ID�Ƿ����������Ӧ�õ�appid������ΪString��
		//mTencent = Tencent.createInstance(APP_ID, this.getApplicationContext());
		// 1.4�汾:�˴�����������������Ӧ�ó����ȫ��context����ͨ��activity��getApplicationContext������ȡ
		// ��ʼ����ͼ
		initViews();
		
		
		bestHistoryRecordPreference = getSharedPreferences(PROFILE_INFO_PREFERENCES, Context.MODE_PRIVATE);
		logState = bestHistoryRecordPreference.getBoolean("LOGSTATE", false);
		

		
		
	}
	@Override
	protected void onStart() {
		super.onStart();
		//handle user profile info

	}
	
	
	private void initViews() {
		
		userSummaryInfo = (LinearLayout) findViewById(R.id.user_summary_info);
		userIcon = (ImageView)findViewById(R.id.img_user_avatar);
		login = (LinearLayout)findViewById(R.id.login);
		logout = (LinearLayout)findViewById(R.id.logout);
		editInfo = (TextView)findViewById(R.id.info_edit);
		userNickname = (TextView)findViewById(R.id.pro_nickname);
		totalDistance = (TextView)findViewById(R.id.total_distance);
		totalTime = (TextView)findViewById(R.id.total_time);
		totalCalories = (TextView)findViewById(R.id.total_calories);

		//handle the bestest history record. Start.
		fastestSpeedMatch = (TextView)findViewById(R.id.fastest_speed_match);
		longestDistance = (TextView)findViewById(R.id.longest_distance);
		longestTime = (TextView)findViewById(R.id.longest_time);
		shortestTimeFive = (TextView)findViewById(R.id.shortest_time_five);
		shortestTimeTen = (TextView)findViewById(R.id.shortest_time_ten);
		shortestTimeHalfMarathon = (TextView)findViewById(R.id.shortest_time_half_marathon);
		shortestTimeFullMarathon = (TextView)findViewById(R.id.shortest_time_full_marathon);
		
		//run history record
		historyTimes = (TextView)findViewById(R.id.record_history_times);
		checkHistoryRecord = (RelativeLayout) findViewById(R.id.run_history);
		
		//More setup
		moreSetup = (RelativeLayout) findViewById(R.id.more_setUp);
		
		handleLogin();			
		
		//Login Dialog 
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Do you want to login ?")
			   .setCancelable(false)
			   .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int id) {
				            logState = true;
				               handleLogin();
				           }
				     })
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						logState = false;
						handleLogin();
						dialog.cancel();
						}
					});
			 alert = builder.create();
		// show Login dialog
		userSummaryInfo.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
				// TODO Auto-generated method stub
				alert.show();
		}
		});
		
		//Enter Edit user Info Activity
		editInfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setAction(Constants.ACTION_EDIT_USER_INFO);
				startActivity(intent);
			}
		});
		
		//Enter check history record Activity
		checkHistoryRecord.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setAction(Constants.ACTION_CHECK_RECORD);
				startActivity(intent);
			}
		});
		
		//Enter More setup choice Activity
		moreSetup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setAction(Constants.ACTION_SETUP);
				startActivity(intent);
			}
		});
	}

	private void handleLogin()
	{ 
		
		
		if(logState) {
			//userIcon.setImageResource(R.id.user_icon);
			logout.setVisibility(View.GONE);
			login.setVisibility(View.VISIBLE);
			
			//user simple Info
			userNickname.setText(
					bestHistoryRecordPreference.getString(NICKNAME, "superman"));
			totalDistance.setText(
					bestHistoryRecordPreference.getString(TOTALDISTANCE, "none"));
			totalTime.setText(
					bestHistoryRecordPreference.getString(TOTALTIME, "none"));
			totalCalories.setText(
					bestHistoryRecordPreference.getString(TOTALCALORIES, "none"));
			
			
			//the best Record
			fastestSpeedMatch.setText(
					bestHistoryRecordPreference.getString(SPEEDMATCH, "nothing"));
			longestDistance.setText(
					bestHistoryRecordPreference.getString(LONGESTDISTANCE, "nothing"));
			longestTime.setText(
					bestHistoryRecordPreference.getString(LONGESTTIME, "nothing"));
			shortestTimeFive.setText(
					bestHistoryRecordPreference.getString(SHORTESTTIME_5, "nothing"));
			shortestTimeTen.setText(
					bestHistoryRecordPreference.getString(SHORTESTTIME_10, "nothing"));
			shortestTimeHalfMarathon.setText(
					bestHistoryRecordPreference.getString(SHORTESTTIME_HM, "nothing"));
			shortestTimeFullMarathon.setText(
					bestHistoryRecordPreference.getString(SHORTESTTIME_FM, "nothing"));
		
			//the history run times
			historyTimes.setText(
					bestHistoryRecordPreference.getInt(HISTORYTIMES, 0) + "��");
		} else {
			login.setVisibility(View.GONE);
			logout.setVisibility(View.VISIBLE);
		}
		
	}
	
	/**
	 *  ����QQ��¼�ӿ�
	 * **/
	/*
	public void login()
	{
		mTencent = Tencent.createInstance(AppId, this.getApplicationContext());
		if (!mTencent.isSessionValid())
		{
			mTencent.login(this, Scope, listener);
		}
	}
	*/
	/**
	 * ����QQע���ӿ�
	 * */
	public void logout()
	{
		mTencent.logout(this);
	}
	
	
	/**
	 * Ӧ�õ���Andriod_SDK�ӿ�ʱ�����Ҫ�ɹ����յ��ص���
	 * ��Ҫ�ڵ��ýӿڵ�Activity��onActivityResult�������������´��룺
	 * 
	 * **/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	mTencent.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * ����SDK�Ѿ���װ�õĽӿ�ʱ�����磺��¼������֧����¼��Ӧ�÷���Ӧ������Ƚӿڣ��贫��ûص���ʵ����
	 * */
	/**
	private class BaseUiListener implements IUiListener {
	
		��¼�ɹ������public void onComplete(JSONObject arg0) �ش���JsonObject�� ���а���OpenId�� AccessToken����Ҫ���ݡ�
		{
			"ret":0,
			"pay_token":"xxxxxxxxxxxxxxxx",
			"pf":"openmobile_android",
			"expires_in":"7776000",
			"openid":"xxxxxxxxxxxxxxxxxxx",
			"pfkey":"xxxxxxxxxxxxxxxxxxx",
			"msg":"sucess",
			"access_token":"xxxxxxxxxxxxxxxxxxxxx"
		}
	
		@Override
		public void onComplete(JSONObject response) {
		mBaseMessageText.setText("onComplete:");
		mMessageText.setText(response.toString());
		doComplete(response);
		}
		protected void doComplete(JSONObject values) {
		}
		@Override
		public void onError(UiError e) {
		showResult("onError:", "code:" + e.errorCode + ", msg:"
		+ e.errorMessage + ", detail:" + e.errorDetail);
		}
		@Override
		public void onCancel() {
		showResult("onCancel", "");
		}
		}
	
	**/
	
	/***
	 * ʹ��requestAsync��request��ͨ�÷�������sdkδ��װ�Ľӿ�ʱ�������ϴ�ͼƬ���鿴���ȣ��贫��ûص���ʵ����
	 * IRequestListener��ʵ��ʾ���������£�
	 * 
	 * **/
	/**
	private class BaseApiListener implements IRequestListener {
		@Override
		public void onComplete(final JSONObject response, Object state) {
		showResult("IRequestListener.onComplete:", response.toString());
		doComplete(response, state);
		}
		protected void doComplete(JSONObject response, Object state) {
		}
		@Override
		public void onIOException(final IOException e, Object state) {
		showResult("IRequestListener.onIOException:", e.getMessage());
		}
		@Override
		public void onMalformedURLException(final MalformedURLException e,
		Object state) {
		showResult("IRequestListener.onMalformedURLException", e.toString());
		}
		@Override
		public void onJSONException(final JSONException e, Object state) {
		showResult("IRequestListener.onJSONException:", e.getMessage());
		}
		@Override
		public void onConnectTimeoutException(ConnectTimeoutException arg0,
		Object arg1) {
		// TODO Auto-generated method stub
		}
		@Override
		public void onSocketTimeoutException(SocketTimeoutException arg0,
		Object arg1) {
		// TODO Auto-generated method stub
		}
		//1.4�汾��IRequestListener ���������쳣
		@Override
		public void onNetworkUnavailableException(NetworkUnavailableException e, Object state){
		// ��ǰ���粻����ʱ�������쳣
		}
		@Override
		public void onHttpStatusException(HttpStatusException e, Object state) {
		// http���󷵻����200ʱ�������쳣
		}
		public void onUnknowException(Exception e, Object state) {
		// ����δ֪����ʱ�ᴥ�����쳣
		}
		}
    **/
	
	
	
	
}
