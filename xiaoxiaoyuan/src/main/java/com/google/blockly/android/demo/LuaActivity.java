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
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Path;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.HighLight;
import com.google.blockly.android.AbstractBlocklyActivity;
import com.google.blockly.android.CategorySelectorFragment;
import com.google.blockly.android.WorkspaceFragment;
import com.google.blockly.android.codegen.CodeGenerationRequest;
import com.google.blockly.android.codegen.LanguageDefinition;
import com.google.blockly.model.DefaultBlocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Demo activity that programmatically adds a view to split the screen between the Blockly workspace
 * and an arbitrary other view or fragment.
 */
public class LuaActivity extends AbstractBlocklyActivity {
    private WebView webView;
    private Button btnRun;
    private Button btnReturn;
    private Button btnSave;
    private Button btnClear;
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
    }
    @Override
    protected View onCreateContentView(int parentId) {
        View root = getLayoutInflater().inflate(R.layout.split_content, null);
        mGeneratedTextView = (TextView) root.findViewById(R.id.generated_code);
        updateTextMinWidth();

        mNoCodeText = mGeneratedTextView.getText().toString(); // Capture initial value.

        btnRun=(Button) root.findViewById(R.id.btn_run);
        btnReturn=(Button)root.findViewById(R.id.btn_return);
        btnClear=(Button)root.findViewById(R.id.btn_clear);
        btnSave=(Button)root.findViewById(R.id.btn_save);


        btnRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llTan=findViewById(R.id.auto_tan);

                LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
                llTan.setLayoutParams(layoutParams);

                layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,0.4f);
                mGeneratedTextView.setLayoutParams(layoutParams);

                webView=findViewById(R.id.wv);
                layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,0.25f);
                webView.setLayoutParams(layoutParams);


                llFlash=findViewById(R.id.ll_flash);
                layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,0.7f);
                llFlash.setLayoutParams(layoutParams);

                onRunCode();
            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
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

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout linearLayout=findViewById(R.id.ll_left);
                LinearLayout linearLayout1=findViewById(R.id.ll_center);
                LinearLayout linearLayout2=findViewById(R.id.ll_function);
                NewbieGuide.with(LuaActivity.this)
                        .alwaysShow(true)
                        .setLabel("guide1")
                        .addGuidePage(GuidePage.newInstance()
                                .addHighLight(linearLayout)
                                .setLayoutRes(R.layout.guide)
                        )
                        .addGuidePage(GuidePage.newInstance()
                                .addHighLight(linearLayout1)
                                .setLayoutRes(R.layout.guide1))
                        .addGuidePage(GuidePage.newInstance()
                                .addHighLight(btnReturn)
                                .setLayoutRes(R.layout.guide2))
                        .addGuidePage(GuidePage.newInstance()
                                .addHighLight(btnClear)
                                .setLayoutRes(R.layout.guide4))
                        .addGuidePage(GuidePage.newInstance()
                                .addHighLight(btnRun)
                                .setLayoutRes(R.layout.guide5))
                        .addGuidePage(GuidePage.newInstance()
                                .addHighLight(linearLayout2)
                                .setLayoutRes(R.layout.guide6))
                        .addGuidePage(GuidePage.newInstance()
                                .addHighLight(btnSave)
                                .setLayoutRes(R.layout.guide3))
                        .show();
            }
        });
        return root;
    }

    @Override
    protected int getActionBarMenuResId() {
        return R.menu.split_actionbar;
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
