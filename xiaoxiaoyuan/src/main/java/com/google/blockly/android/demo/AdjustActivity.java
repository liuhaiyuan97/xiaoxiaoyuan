/*
 *  Copyright 2017 Google Inc. All Rights Reserved.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.google.blockly.android.demo;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.blockly.android.AbstractBlocklyActivity;
import com.google.blockly.android.codegen.CodeGenerationRequest;
import com.google.blockly.android.codegen.LanguageDefinition;
import com.google.blockly.model.DefaultBlocks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Demo activity that programmatically adds a view to split the screen between the Blockly workspace
 * and an arbitrary other view or fragment.
 */
public class AdjustActivity extends AbstractBlocklyActivity {
    private int n=0;
    private WebView webView;
    private Button btnRun;
    private Button btnReturn;
    private Button btnAdjust;
    private Button btnClear;
    private Button btnQuestion;
    private int x=0;
    private int y=0;
    private int width=1;
    private int height=1;
    private int rotation=0;
    private static final String TAG = "LuaActivity";
    private LinearLayout llTan;
    private LinearLayout llFlash;
    private List<Integer> If;
    private List<Integer> For;
    private List<Integer> Else;
    private List<Integer> ElseIf;
    private List<Integer> Var;
    private List<Integer> Int;
    private List<Integer> Cal;
    private List<Integer> Str;
    private String code;
    private static OkHttpClient okHttpClient;
    private static int userid=0;


    private static final String SAVE_FILENAME = "lua_workspace.xml";
    private static final String AUTOSAVE_FILENAME = "lua_workspace_temp.xml";
    // Add custom blocks to this list.
    private static final List<String> BLOCK_DEFINITIONS = DefaultBlocks.getAllBlockDefinitions();
    private static final List<String> LUA_GENERATORS = Arrays.asList();

    private static final LanguageDefinition LUA_LANGUAGE_DEF
            = new LanguageDefinition("javascript_compressed.js", "Blockly.JavaScript");

    private TextView mGeneratedTextView;
    private Handler mHandler;

    private String mNoCodeText;
    private static String user="";
    private static int floor=1;
    private static int currentFloor=1;
    private String questionContent="";
    private String questionResult="";
    private boolean state=false;



    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    //这里是点运行以后输出在右边的语句的方法
    CodeGenerationRequest.CodeGeneratorCallback mCodeGeneratorCallback =
            new CodeGenerationRequest.CodeGeneratorCallback() {
                @Override
                public void onFinishCodeGeneration(final String generatedCode) {
                    mHandler.post(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
                            mGeneratedTextView.setText(generatedCode);
                            code=generatedCode;
                            WebCreate(generatedCode);
                            clearArray();
                            ChangeColor changeColor=new ChangeColor();
                            changeColor.color(If,Else,ElseIf,For,Var,Int,Cal,Str,0,0,0,0,0,0,0,0,generatedCode,mGeneratedTextView);
                            if(generatedCode.contains("left")||generatedCode.contains("right")||generatedCode.contains("down")||generatedCode.contains("up")){
                                mGeneratedTextView.setText("");
                            }
                            if(!generatedCode.equals("")) {
                                flash(generatedCode);
                            }

                            updateTextMinWidth();
                        }
                    });
                }
            };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return TurtleActivity.onDemoItemSelected(item, this) || super.onOptionsItemSelected(item);
    }

    private void clearArray(){
        If=new ArrayList<Integer>();
        Else=new ArrayList<Integer>();
        ElseIf=new ArrayList<Integer>();
        Var=new ArrayList<Integer>();
        For=new ArrayList<Integer>();
        Int=new ArrayList<Integer>();
        Cal=new ArrayList<Integer>();
        Str=new ArrayList<Integer>();
    }

    public void WebCreate(String code){
        String a="";
        int i=code.indexOf("\n,");
        if(i!=-1) {
            a = code.substring(0, i + 1);
        }else{
            a=code.substring(0,code.length());
        }
        webView=findViewById(R.id.wv);
        webView.getSettings().setJavaScriptEnabled(true);
        StringBuffer str=new StringBuffer();
        str.append("<html>\n" +
                "<head>\n" +
                "\t<title>code</title>\n" +
                "\t<script type=\"text/javascript\">\n" +
                "\t\t\tfunction a(){\n" +
                "var c=document.getElementById(\"req\");\n" +
                "\t\t\t\tc.style.color=\"White\";" +
                "c.style.display=\"inline\";" +
                "var array=[];\n" +
                "\t\t\tvar t=document.getElementById(\"t\");");
        str.append(a);
        str.append("return t.value;\n" +
                "\t\t}" +
                "\t</script>\n" +
                "</head>\n" +
                "<body onLoad=\"a()\">\n" +
                "\t<form>\n" +
                "\t\t<p id=\"req\">结果:</p>" +
                "<input type=\"test\" id=\"t\" disabled=\"disabled\" />\n" +
                "\t</form>\n" +
                "</body>\n" +
                "</html>");
        webView.loadDataWithBaseURL(null, str.toString(), "text/html", "utf-8", null);
        webView.setBackgroundColor(0);
        webView.loadDataWithBaseURL(null, str.toString(), "text/html", "utf-8", null);
        webView.setBackgroundColor(0);

    }

    public void WebClean(){
        WebView webView=findViewById(R.id.wv);
        webView.getSettings().setJavaScriptEnabled(true);
        StringBuffer str=new StringBuffer();
        str.append("<html>\n" +
                "<head>\n" +
                "\t<title>111</title>\n" +
                "\t<script type=\"text/javascript\">\n" +
                "\t</script>\n" +
                "</head>\n" +
                "<body>\n" +
                "\t\n" +
                "\t<form>\n" +
                "\t\t结果:<textarea  rows=\"1\" id=\"t\" type=\"text\" disabled=\"disabled\"></textarea>\n" +
                "\t</form>\n" +
                "</body>\n" +
                "</html>");
        webView.loadDataWithBaseURL(null,str.toString(),"text/html","utf-8",null);
        webView.setBackgroundColor(0);
    }

    public int getMove(String code){
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(code);
        m.find();
        String a = m.group();
        int n = Integer.parseInt(a);
        return n;

    }

    public void flash(String code){
        ImageView imageView=findViewById(R.id.generated_move);
        int n = 0;
        boolean fl=true;
        List<Animator> animators = new ArrayList<Animator>();
        ObjectAnimator o = null;
        String[] list = code.split(";");
        for (int i = 0; i < list.length - 1; i++) {
            if (list[i].contains("left")) {
                n=getMove(list[i]);
                o = ObjectAnimator.ofFloat(imageView, "translationX", x, x - n);
                x = x - n;
            } else if(list[i].contains("right")){
                n=getMove(list[i]);
                o = ObjectAnimator.ofFloat(imageView, "translationX", x, x + n);
                x = x + n;
            } else if(list[i].contains("up")){
                n=getMove(list[i]);
                o = ObjectAnimator.ofFloat(imageView, "translationY", y, y- n);
                y = y - n;
            } else if(list[i].contains("down")){
                n=getMove(list[i]);
                o = ObjectAnimator.ofFloat(imageView, "translationY", y, y+ n);
                y = y + n;
            } else if(list[i].contains("bigwidth")){
                n=getMove(list[i]);
                o = ObjectAnimator.ofFloat(imageView, "scaleX",width,n*width);
                width=n*width;
            } else if(list[i].contains("bigheight")){
                n=getMove(list[i]);
                o = ObjectAnimator.ofFloat(imageView, "scaleY",height,n*height);
                height=n*height;
            } else if(list[i].contains("returnwidth")){
                o=ObjectAnimator.ofFloat(imageView,"scaleX",width,1);
                width=1;
            } else if(list[i].contains("returnheight")){
                o=ObjectAnimator.ofFloat(imageView,"scaleY",height,1);
                height=1;
            }else if(list[i].contains("rotate")){
                n=getMove(list[i]);
                o=ObjectAnimator.ofFloat(imageView,"rotation",rotation,rotation+n);
                rotation=rotation+n;
            }
            else{
                fl=false;
                break;
            }
            animators.add(o);
        }
        if(fl==true) {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playSequentially(animators);
            animatorSet.setDuration(1000);
            animatorSet.start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebClean();
        Window window = getWindow();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //隐藏状态栏
        //定义全屏参数
        int flag= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);
        mHandler = new Handler();

        Intent intent=getIntent();
        user=intent.getStringExtra("user");
        floor=intent.getIntExtra("floor",1);
        currentFloor=intent.getIntExtra("currentFloor",1);
        Log.e("AdjustActivity","floor:"+floor+" currentFloor:"+currentFloor);
        questionContent=intent.getStringExtra("questionContent");
        questionResult=intent.getStringExtra("questionResult");
        Log.e("useridlate",intent.getIntExtra("userid",0)+"");
        userid=intent.getIntExtra("userid",0);
    }

    @Override
    protected View onCreateContentView(int parentId) {
        View root = getLayoutInflater().inflate(R.layout.activity_adjust, null);
        mGeneratedTextView = (TextView) root.findViewById(R.id.generated_code);
        updateTextMinWidth();

        mNoCodeText = mGeneratedTextView.getText().toString(); // Capture initial value.

        btnRun=(Button) root.findViewById(R.id.btn_run);
        btnReturn=(Button)root.findViewById(R.id.btn_return);
        btnClear=(Button)root.findViewById(R.id.btn_clear);
        btnAdjust=(Button)root.findViewById(R.id.btn_adjust);
        btnQuestion=(Button)root.findViewById(R.id.btn_question);
        btnRun.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                llTan=findViewById(R.id.auto_tan);

                LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
                llTan.setLayoutParams(layoutParams);

                layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,0.4f);
                mGeneratedTextView.setLayoutParams(layoutParams);

                webView=findViewById(R.id.wv);
                layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,0.25f);
                webView.setLayoutParams(layoutParams);

                llFlash=findViewById(R.id.ll_flash);
                layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,1.0f);
                llFlash.setLayoutParams(layoutParams);
                onRunCode();
                n++;
            }
        });

        btnReturn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onClearWorkspace();
                WebClean();

                llTan=findViewById(R.id.auto_tan);
                LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 0.0f);
                llTan.setLayoutParams(layoutParams);

                ImageView imageView=findViewById(R.id.generated_move);
                ObjectAnimator o1=ObjectAnimator.ofFloat(imageView,"translationX",0).setDuration(0);
                ObjectAnimator o2=ObjectAnimator.ofFloat(imageView,"translationY",0).setDuration(0);
                ObjectAnimator o3=ObjectAnimator.ofFloat(imageView,"scaleX",1).setDuration(0);
                ObjectAnimator o4=ObjectAnimator.ofFloat(imageView,"scaleY",1).setDuration(0);
                ObjectAnimator o5=ObjectAnimator.ofFloat(imageView,"rotation",0).setDuration(0);
                x=0;
                y=0;
                width=1;
                height=1;
                rotation=0;
                o1.start();
                o2.start();
                o3.start();
                o4.start();
                o5.start();
            }
        });

        btnAdjust.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if(n!=0) {
                    webView.evaluateJavascript("javascript:a()", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            int len=value.length();
                            String result=value.substring(1,len-1);
                            Log.e("AdjustActivity","正确结果 questionResult:"+questionResult);
                            Log.e("AdjustActivity","此时获取用户运行结果 result:"+result);
                            if(result.equals(questionResult)){
                                if(code.contains("array.push('"+questionResult+"')") && !questionResult.equals("Hello World")){
                                    Toast.makeText(AdjustActivity.this,"不能直接打印结果哦",Toast.LENGTH_SHORT).show();
                                }else {
                                    Log.e("AdjustActivity", "此时用户运行结果正确");
                                    if (currentFloor == floor) {
                                        //此时用户当前闯关就是最大关数
                                        Log.e("AdjustActivity", "此时floor=currentFloor运行异步类");
                                        AddOneFloorTASK addOneFloorTASK = new AddOneFloorTASK(userid, floor);
                                        addOneFloorTASK.execute();
                                    }
                                    WinCustomAlertDialog winDialog = new WinCustomAlertDialog();
                                    winDialog.setCancelable(false);
                                    winDialog.show(getSupportFragmentManager(), "");
                                }
                            }else{
                                Log.e("AdjustActivity","此时用户运行结果错误");
                                LoseCustomAlertDialog loseDialog=new LoseCustomAlertDialog();
                                loseDialog.setCancelable(false);
                                loseDialog.show(getSupportFragmentManager(),"");

                            }

                        }
                    });
                    n=0;
                }else{
                    Toast.makeText(AdjustActivity.this,"请先运行再判断哦",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnQuestion.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                state=!state;
                ShowPopUpWindow();
            }
        });
        return root;
    }
    /**
     * 闯关通过改变关数异步类
     */
    public static class AddOneFloorTASK extends AsyncTask{
        private int id;
        private int floor;
        public AddOneFloorTASK(int id,int floor){
            this.id=id;
            this.floor=floor;
        }
        @Override
        protected Object doInBackground(Object[] objects) {
//            try {
//                int nextFloor=floor+1;
//                URL url=new URL("http://60.205.183.222:8080/xiaoxiaoyuan/addOneFloorByUserServlet?user="+user+"&floor="+nextFloor);
//                Log.e("AdjustActivity","user:"+user+" nextFloor:"+nextFloor);
//                HttpURLConnection connection= (HttpURLConnection)url.openConnection();
//                connection.setRequestProperty("content-type","utf-8");
//                Log.e("AdjustActivity","此时获取URL，创建连接");
//
//                InputStream inputStream=connection.getInputStream();
//                InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
//                BufferedReader reader=new BufferedReader(inputStreamReader);
//                String str=reader.readLine();
//                Log.e("AdjustActivity","输入流中的信息："+str);
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
                int nextFloor=floor+1;
                okHttpClient=new OkHttpClient();
                Request request=new Request.Builder()
                                .url("http://192.168.43.179:8080/xiaoxiaoyuanssm/user/addUserFloor?id="+userid+"&floor="+nextFloor)
                                .build();

                Call call=okHttpClient.newCall(request);
            try {
                Response response=call.execute();
                String str=response.body().string();
                String sum=str.substring(1,str.length()-1);
                if(sum.equals("true")){
                    Log.e("true","true");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }

    /**
     * 将题目内容以popupWindow形式显示
     */
    public void ShowPopUpWindow(){

        View view =getLayoutInflater().inflate(R.layout.layout_question_content,null);
        TextView tvSeeQuestionContent=view.findViewById(R.id.tv_see_question_content);
        tvSeeQuestionContent.setText(questionContent);

        final PopupWindow popupWindow=new PopupWindow(AdjustActivity.this);
        popupWindow.setWidth(dip2px(AdjustActivity.this,300));
        popupWindow.setHeight(dip2px(AdjustActivity.this,200));
        popupWindow.setContentView(view);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        popupWindow.showAsDropDown(btnQuestion, -200, 0);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindow.dismiss();
            }
        });



    }

    /**
     * 闯关失败弹出框
     */
    @SuppressLint("ValidFragment")
    public static class LoseCustomAlertDialog extends DialogFragment{
        private Button btnJump;
        private Button btnTry;
        private ImageView ivLogo1;
        private ImageView ivLogo2;
        public View onCreateView(@NonNull LayoutInflater inflater,
                                 @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {

            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            View view=inflater.inflate(R.layout.layout_lose_alertdialog,null);
            btnJump=view.findViewById(R.id.btn_dialog_jump);
            btnTry=view.findViewById(R.id.btn_dialog_try);
            ivLogo1 = view.findViewById(R.id.iv_lose1);
            ivLogo2 = view.findViewById(R.id.iv_lose2);

            ObjectAnimator o1=ObjectAnimator.ofFloat(ivLogo1,"rotation",0,-360).setDuration(2000);
            //o1.setRepeatCount(ValueAnimator.INFINITE);
            o1.start();

            ObjectAnimator o2=ObjectAnimator.ofFloat(ivLogo2,"rotation",0,360).setDuration(2000);
            //o2.setRepeatCount(ValueAnimator.INFINITE);
            o2.start();

            LoseCustomAlertDialogListener listener=new LoseCustomAlertDialogListener();
            btnJump.setOnClickListener(listener);
            btnTry.setOnClickListener(listener);
            return view;
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onResume() {
            super.onResume();
            getDialog().getWindow().setLayout(dip2px(getContext(),400),dip2px(getContext(),310));
        }

        public class LoseCustomAlertDialogListener implements OnClickListener{
            @Override
            public void onClick(View view) {
                switch(view.getId()){
                    case R.id.btn_dialog_jump:
                        getDialog().dismiss();
                        if(floor==currentFloor){
                            AddOneFloorTASK addOneFloorTASK=new AddOneFloorTASK(userid,floor);
                            addOneFloorTASK.execute();
                        }
                        Intent intent=new Intent();
                        intent.putExtra("user",user);
                        intent.setClass(getContext(),RunActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.btn_dialog_try:
                        getDialog().dismiss();
                        break;

                }
            }
        }
    }

    /**
     * 闯关成功弹出框
     */
    public static class WinCustomAlertDialog extends DialogFragment{
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater,
                                 @Nullable ViewGroup container,
                                 Bundle savedInstanceState) {
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            View view =inflater.inflate(R.layout.layout_win_alertdialog,null);
            Button btnDialogNext=view.findViewById(R.id.btn_dialog_next);
            ImageView ivLogo1 = view.findViewById(R.id.iv_win1);
            ImageView ivLogo2 = view.findViewById(R.id.iv_win2);

            ObjectAnimator o1=ObjectAnimator.ofFloat(ivLogo1,"rotation",0,-360).setDuration(4000);
            o1.setRepeatCount(ValueAnimator.INFINITE);
            o1.start();

            ObjectAnimator o2=ObjectAnimator.ofFloat(ivLogo2,"rotation",0,360).setDuration(4000);
            o2.setRepeatCount(ValueAnimator.INFINITE);
            o2.start();

            WinCustomAlertDialogListener listener=new WinCustomAlertDialogListener();
            btnDialogNext.setOnClickListener(listener);

            return view;
        }

        @Override
        public void onResume() {
            super.onResume();
            getDialog().getWindow().setLayout(dip2px(getContext(),400),dip2px(getContext(),310));
        }

        public class WinCustomAlertDialogListener implements OnClickListener{
            @Override
            public void onClick(View view) {
                switch(view.getId()){
                    case R.id.btn_dialog_next:
                        getDialog().dismiss();
                        Intent intent=new Intent();
                        intent.putExtra("user",user);
                        intent.setClass(getContext(),RunActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        }
    }

    /**
     * 将dp转换成dx
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    @NonNull
    @Override
    protected List<String> getBlockDefinitionsJsonPaths() {
        return BLOCK_DEFINITIONS;
    }

    @NonNull
    @Override
    protected LanguageDefinition getBlockGeneratorLanguage() {
        return LUA_LANGUAGE_DEF;
    }

    @NonNull
    @Override
    protected String getToolboxContentsXmlPath() {
        return DefaultBlocks.TOOLBOX_PATH;
    }

    @NonNull
    @Override
    protected List<String> getGeneratorsJsPaths() {
        return LUA_GENERATORS;
    }

    @NonNull
    @Override
    protected CodeGenerationRequest.CodeGeneratorCallback getCodeGenerationCallback() {
        // Uses the same callback for every generation call.
        return mCodeGeneratorCallback;
    }

    @Override
    public void onClearWorkspace() {
        super.onClearWorkspace();
        mGeneratedTextView.setText(mNoCodeText);
        updateTextMinWidth();
    }

    /**
     * Estimate the pixel size of the longest line of text, and set that to the TextView's minimum
     * width.
     */
    private void updateTextMinWidth() {
        String text = mGeneratedTextView.getText().toString();
        int maxline = 0;
        int start = 0;
        int index = text.indexOf('\n', start);
        while (index > 0) {
            maxline = Math.max(maxline, index - start);
            start = index + 1;
            index = text.indexOf('\n', start);
        }
        int remainder = text.length() - start;
        if (remainder > 0) {
            maxline = Math.max(maxline, remainder);
        }

        float density = getResources().getDisplayMetrics().density;
        mGeneratedTextView.setMinWidth((int) (maxline * 13 * density));
    }

    /**
     * Optional override of the save path, since this demo Activity has multiple Blockly
     * configurations.
     * @return Workspace save path used by this Activity.
     */
    @Override
    @NonNull
    protected String getWorkspaceSavePath() {
        return SAVE_FILENAME;
    }

    /**
     * Optional override of the auto-save path, since this demo Activity has multiple Blockly
     * configurations.自动保存路径的可选覆盖，因为此演示Activity具有多个Blockly配置。
     * @return Workspace auto-save path used by this Activity.此活动使用的工作区自动保存路径
     */
    @Override
    @NonNull
    protected String getWorkspaceAutosavePath() {
        return AUTOSAVE_FILENAME;
    }
}
