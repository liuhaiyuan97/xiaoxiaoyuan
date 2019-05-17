package com.google.blockly.android.demo;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import java.util.List;

public class ChangeColor {
    public void color(List<Integer> If,List<Integer> Else,List<Integer> ElseIf,List<Integer> For,List<Integer> Var,List<Integer> Int,List<Integer> Cal,List<Integer> Str,int Ifcurrent,int Elsecurrent,int ElseIfcurrent,int Varcurrent,int Forcurrent,int Intcurrent,int Calcurrent,int Strcurrent,String a, TextView textView){
        //If
        while(a.indexOf("if",Ifcurrent)!=-1) {
            if(If.size()==0) {
                If.add(a.indexOf("if", 0));
            }else{
                int n=If.size()-1;
                if(If.get(n)!=a.indexOf("if",Ifcurrent)){
                    If.add(a.indexOf("if", Ifcurrent));
                }
            }
            Ifcurrent+=1;
        }
        Ifcurrent=0;
        //Else
        while(a.indexOf("else",Elsecurrent)!=-1) {
            if(Else.size()==0) {
                Else.add(a.indexOf("else", 0));
            }else{
                int n=Else.size()-1;
                if(Else.get(n)!=a.indexOf("else",Elsecurrent)){
                    Else.add(a.indexOf("else", Elsecurrent));
                }
            }
            Elsecurrent+=1;
        }
        Elsecurrent=0;
        //ElseIf
        while(a.indexOf("else if",ElseIfcurrent)!=-1) {
            if(ElseIf.size()==0) {
                ElseIf.add(a.indexOf("else if", 0));
            }else{
                int n=ElseIf.size()-1;
                if(ElseIf.get(n)!=a.indexOf("else if",ElseIfcurrent)){
                    ElseIf.add(a.indexOf("else if", ElseIfcurrent));
                }
            }
            ElseIfcurrent+=1;
        }
        ElseIfcurrent=0;
        //For
        while(a.indexOf("for",Forcurrent)!=-1) {
            if(For.size()==0) {
                For.add(a.indexOf("for", 0));
            }else{
                int n=For.size()-1;
                if(For.get(n)!=a.indexOf("for",Forcurrent)){
                    For.add(a.indexOf("for", Forcurrent));
                }
            }
            Forcurrent+=1;
        }
        Forcurrent=0;
        //Var
        while(a.indexOf("var",Varcurrent)!=-1) {
            if(Var.size()==0) {
                Var.add(a.indexOf("var", 0));
            }else{
                int n=Var.size()-1;
                if(Var.get(n)!=a.indexOf("var",Varcurrent)){
                    Var.add(a.indexOf("var", Varcurrent));
                }
            }
            Varcurrent+=1;
        }
        Varcurrent=0;
        //数字
        while (a.indexOf(1+"", Intcurrent) != -1) {
            if (Int.size() == 0) {
                Int.add(a.indexOf(1+"", 0));
            } else {
                int n = Int.size() - 1;
                if (Int.get(n) != a.indexOf(1+"", Intcurrent)) {
                    Int.add(a.indexOf(1+"", Intcurrent));
                }
            }
                Intcurrent += 1;
            }
        Intcurrent=0;
        while (a.indexOf(2+"", Intcurrent) != -1) {
            if (Int.size() == 0) {
                Int.add(a.indexOf(2+"", 0));
            } else {
                int n = Int.size() - 1;
                if (Int.get(n) != a.indexOf(2+"", Intcurrent)) {
                    Int.add(a.indexOf(2+"", Intcurrent));
                }
            }
            Intcurrent += 1;
        }
        Intcurrent=0;
        while (a.indexOf(3+"", Intcurrent) != -1) {
            if (Int.size() == 0) {
                Int.add(a.indexOf(3+"", 0));
            } else {
                int n = Int.size() - 1;
                if (Int.get(n) != a.indexOf(3+"", Intcurrent)) {
                    Int.add(a.indexOf(3+"", Intcurrent));
                }
            }
            Intcurrent += 1;
        }
        Intcurrent=0;
        while (a.indexOf(4+"", Intcurrent) != -1) {
            if (Int.size() == 0) {
                Int.add(a.indexOf(4+"", 0));
            } else {
                int n = Int.size() - 1;
                if (Int.get(n) != a.indexOf(4+"", Intcurrent)) {
                    Int.add(a.indexOf(4+"", Intcurrent));
                }
            }
            Intcurrent += 1;
        }
        Intcurrent=0;
        while (a.indexOf(5+"", Intcurrent) != -1) {
            if (Int.size() == 0) {
                Int.add(a.indexOf(5+"", 0));
            } else {
                int n = Int.size() - 1;
                if (Int.get(n) != a.indexOf(5+"", Intcurrent)) {
                    Int.add(a.indexOf(5+"", Intcurrent));
                }
            }
            Intcurrent += 1;
        }
        Intcurrent=0;
        while (a.indexOf(6+"", Intcurrent) != -1) {
            if (Int.size() == 0) {
                Int.add(a.indexOf(6+"", 0));
            } else {
                int n = Int.size() - 1;
                if (Int.get(n) != a.indexOf(6+"", Intcurrent)) {
                    Int.add(a.indexOf(6+"", Intcurrent));
                }
            }
            Intcurrent += 1;
        }
        Intcurrent=0;
        while (a.indexOf(7+"", Intcurrent) != -1) {
            if (Int.size() == 0) {
                Int.add(a.indexOf(7+"", 0));
            } else {
                int n = Int.size() - 1;
                if (Int.get(n) != a.indexOf(7+"", Intcurrent)) {
                    Int.add(a.indexOf(7+"", Intcurrent));
                }
            }
            Intcurrent += 1;
        }
        Intcurrent=0;
        while (a.indexOf(8+"", Intcurrent) != -1) {
            if (Int.size() == 0) {
                Int.add(a.indexOf(8+"", 0));
            } else {
                int n = Int.size() - 1;
                if (Int.get(n) != a.indexOf(8+"", Intcurrent)) {
                    Int.add(a.indexOf(8+"", Intcurrent));
                }
            }
            Intcurrent += 1;
        }
        Intcurrent=0;
        while (a.indexOf(9+"", Intcurrent) != -1) {
            if (Int.size() == 0) {
                Int.add(a.indexOf(9+"", 0));
            } else {
                int n = Int.size() - 1;
                if (Int.get(n) != a.indexOf(9+"", Intcurrent)) {
                    Int.add(a.indexOf(9+"", Intcurrent));
                }
            }
            Intcurrent += 1;
        }
        Intcurrent=0;
        while (a.indexOf(0+"", Intcurrent) != -1) {
            if (Int.size() == 0) {
                Int.add(a.indexOf(0+"", 0));
            } else {
                int n = Int.size() - 1;
                if (Int.get(n) != a.indexOf(0+"", Intcurrent)) {
                    Int.add(a.indexOf(0+"", Intcurrent));
                }
            }
            Intcurrent += 1;
        }
        Intcurrent=0;
        //运算calculations
        while (a.indexOf("+", Calcurrent) != -1) {
            if (Cal.size() == 0) {
                Cal.add(a.indexOf("+", 0));
            } else {
                int n = Cal.size() - 1;
                if (Cal.get(n) != a.indexOf("+", Calcurrent)) {
                    Cal.add(a.indexOf("+", Calcurrent));
                }
            }
            Calcurrent += 1;
        }
        Calcurrent=0;
        while (a.indexOf("-", Calcurrent) != -1) {
            if (Cal.size() == 0) {
                Cal.add(a.indexOf("-", 0));
            } else {
                int n = Cal.size() - 1;
                if (Cal.get(n) != a.indexOf("-", Calcurrent)) {
                    Cal.add(a.indexOf("-", Calcurrent));
                }
            }
            Calcurrent += 1;
        }
        Calcurrent=0;

        while (a.indexOf("*", Calcurrent) != -1) {
            if (Cal.size() == 0) {
                Cal.add(a.indexOf("*", 0));
            } else {
                int n = Cal.size() - 1;
                if (Cal.get(n) != a.indexOf("*", Calcurrent)) {
                    Cal.add(a.indexOf("*", Calcurrent));
                }
            }
            Calcurrent += 1;
        }
        Calcurrent=0;

        while (a.indexOf("/", Calcurrent) != -1) {
            if (Cal.size() == 0) {
                Cal.add(a.indexOf("/", 0));
            } else {
                int n = Cal.size() - 1;
                if (Cal.get(n) != a.indexOf("/", Calcurrent)) {
                    Cal.add(a.indexOf("/", Calcurrent));
                }
            }
            Calcurrent += 1;
        }
        Calcurrent=0;

        while (a.indexOf("%", Calcurrent) != -1) {
            if (Cal.size() == 0) {
                Cal.add(a.indexOf("%", 0));
            } else {
                int n = Cal.size() - 1;
                if (Cal.get(n) != a.indexOf("%", Calcurrent)) {
                    Cal.add(a.indexOf("%", Calcurrent));
                }
            }
            Calcurrent += 1;
        }
        Calcurrent=0;
        while (a.indexOf(">", Calcurrent) != -1) {
            if (Cal.size() == 0) {
                Cal.add(a.indexOf(">", 0));
            } else {
                int n = Cal.size() - 1;
                if (Cal.get(n) != a.indexOf(">", Calcurrent)) {
                    Cal.add(a.indexOf(">", Calcurrent));
                }
            }
            Calcurrent += 1;
        }
        Calcurrent=0;

        while (a.indexOf("<", Calcurrent) != -1) {
            if (Cal.size() == 0) {
                Cal.add(a.indexOf("<", 0));
            } else {
                int n = Cal.size() - 1;
                if (Cal.get(n) != a.indexOf("<", Calcurrent)) {
                    Cal.add(a.indexOf("<", Calcurrent));
                }
            }
            Calcurrent += 1;
        }
        Calcurrent=0;

        while (a.indexOf("!", Calcurrent) != -1) {
            if (Cal.size() == 0) {
                Cal.add(a.indexOf("!", 0));
            } else {
                int n = Cal.size() - 1;
                if (Cal.get(n) != a.indexOf("!", Calcurrent)) {
                    Cal.add(a.indexOf("!", Calcurrent));
                }
            }
            Calcurrent += 1;
        }
        Calcurrent=0;
        while (a.indexOf("=", Calcurrent) != -1) {
            if (Cal.size() == 0) {
                Cal.add(a.indexOf("=", 0));
            } else {
                int n = Cal.size() - 1;
                if (Cal.get(n) != a.indexOf("=", Calcurrent)) {
                    Cal.add(a.indexOf("=", Calcurrent));
                }
            }
            Calcurrent += 1;
        }
        Calcurrent=0;
        while (a.indexOf("?", Calcurrent) != -1) {
            if (Cal.size() == 0) {
                Cal.add(a.indexOf("?", 0));
            } else {
                int n = Cal.size() - 1;
                if (Cal.get(n) != a.indexOf("?", Calcurrent)) {
                    Cal.add(a.indexOf("?", Calcurrent));
                }
            }
            Calcurrent += 1;
        }
        Calcurrent=0;
        while (a.indexOf(":", Calcurrent) != -1) {
            if (Cal.size() == 0) {
                Cal.add(a.indexOf(":", 0));
            } else {
                int n = Cal.size() - 1;
                if (Cal.get(n) != a.indexOf(":", Calcurrent)) {
                    Cal.add(a.indexOf(":", Calcurrent));
                }
            }
            Calcurrent += 1;
        }
        Calcurrent=0;

        while (a.indexOf("'", Strcurrent) != -1) {
            if (Str.size() == 0) {
                Str.add(a.indexOf("'", 0));
            } else {
                int n = Str.size() - 1;
                if (Str.get(n) != a.indexOf("'", Strcurrent)) {
                    Str.add(a.indexOf("'", Strcurrent));
                }
            }
            Strcurrent += 1;
        }
        Strcurrent=0;

        SpannableString spannableString = new SpannableString(a);
        //If
        for(int i=0;i<If.size();i++) {
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF0000")),If.get(i),If.get(i)+2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(spannableString);
        }
        //Else
        for(int i=0;i<Else.size();i++) {
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF0000")),Else.get(i),Else.get(i)+4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(spannableString);
        }
        //ElseIf
        for(int i=0;i<ElseIf.size();i++) {
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF0000")),ElseIf.get(i),ElseIf.get(i)+7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(spannableString);
        }
        //Var
        for(int i=0;i<Var.size();i++) {
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#0000FF")),Var.get(i),Var.get(i)+3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(spannableString);
        }
        //For
        for(int i=0;i<For.size();i++) {
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF0000")),For.get(i),For.get(i)+3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(spannableString);
        }
        //数字
        for(int i=0;i<Int.size();i++) {
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#99FF00")),Int.get(i),Int.get(i)+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(spannableString);
        }
        //运算Cal
        for(int i=0;i<Cal.size();i++) {
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF33FF")),Cal.get(i),Cal.get(i)+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(spannableString);
        }
        //字符串
        for(int i=0;i<Str.size()/2;i++) {
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF9900")),Str.get(i*2),Str.get(i*2+1)+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(spannableString);
        }

    }

}
