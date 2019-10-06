package com.example.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class CalculatorActivity extends Activity implements Button.OnClickListener {

    static final String TAG = "Calculator";

    private List<Integer> getButtonIds() {
        return Arrays.asList(R.id.btn_cancel, R.id.btn_divide, R.id.btn_multiply, R.id.btn_clear,
                R.id.btn_seven, R.id.btn_eight, R.id.btn_nine, R.id.btn_add,
                R.id.btn_four, R.id.btn_five, R.id.btn_six, R.id.btn_sub,
                R.id.btn_one, R.id.btn_two, R.id.btn_three, R.id.btn_sqrt,
                R.id.btn_zero, R.id.btn_point, R.id.btn_equal);
    }

    private TextView tv_result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        tv_result = findViewById(R.id.tv_result);

        for (Integer id : getButtonIds()) {
            findViewById(id).setOnClickListener(this);
        }


    }


    @Override
    public void onClick(View v) {
        Integer id = v.getId();

        String input = "";
        if (id == R.id.btn_sqrt) {
            input = "√";
        } else {
            input = ((TextView)v).getText().toString();
        }

        Log.d(TAG, "viewId = " + id + ", input = " + input);

        if (id == R.id.btn_clear) {
            clear();
        } else if (id == R.id.btn_cancel) {
            if (m_operator == Operator.NONE) {
                if (m_firstNum.length() == 1) {
                    m_firstNum = "0";
                } else if (m_firstNum.length() > 0) {
                    m_firstNum = m_firstNum.substring(0, m_firstNum.length() - 1);
                } else {
                    Toast.makeText(this, "没有可以取消的数字了", Toast.LENGTH_SHORT).show();
                    return;
                }
                m_showText = m_firstNum;
            } else {
                if (m_nextNum.length() == 1) {
                    m_nextNum = "";
                } else if (m_nextNum.length() > 0) {
                    m_nextNum = m_nextNum.substring(0, m_nextNum.length() - 1);
                } else {
                    Toast.makeText(this, "没有可以取消的数字了", Toast.LENGTH_SHORT).show();
                    return;
                }

                m_showText = m_showText.substring(0, m_showText.length() - 1);
            }

            tv_result.setText(m_showText);

        } else if (id == R.id.btn_equal) {

            if (m_operator == Operator.NONE || m_operator == Operator.EQUAL) {
                Toast.makeText(this, "请输入运算符", Toast.LENGTH_SHORT).show();
                return;
            } else if (m_nextNum.length() <= 0) {
                Toast.makeText(this, "请输入数字", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!calculate()) {
                return;
            }

            m_operator = Operator.EQUAL;
            m_showText = m_showText + "=" + m_result;
            tv_result.setText(m_showText);

        } else if (id == R.id.btn_add || id == R.id.btn_sub
                || id == R.id.btn_multiply || id == R.id.btn_divide) {

            if (m_firstNum.length() <= 0) {
                Toast.makeText(this, "请输入数字", Toast.LENGTH_SHORT).show();
                return;
            }

            if (m_operator == Operator.NONE
                    || m_operator == Operator.SQRT
                    || m_operator == Operator.EQUAL) {

                if (id == R.id.btn_add) {
                    m_operator = Operator.ADD;
                } else if (id == R.id.btn_sub) {
                    m_operator = Operator.SUB;
                } else if (id == R.id.btn_multiply) {
                    m_operator = Operator.MULTIPLY;
                } else if (id == R.id.btn_divide) {
                    m_operator = Operator.DIVIDE;
                }

                m_showText = m_showText + input;
                tv_result.setText(m_showText);
            } else {
                Toast.makeText(this, "请输入数字", Toast.LENGTH_SHORT).show();
                return;
            }

        } else if (id == R.id.btn_sqrt) {
            if (m_firstNum.length() <= 0) {
                Toast.makeText(this, "请输入数字", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Double.parseDouble(m_firstNum) < 0) {
                Toast.makeText(this, "开根号的数值不能小于0", Toast.LENGTH_SHORT).show();
                return;
            }
            m_result = String.valueOf(Math.sqrt(Double.parseDouble(m_firstNum)));
            m_firstNum = m_result;
            m_nextNum = "";
            m_operator = Operator.SQRT;
            m_showText = m_showText + "√=" + m_result;
            tv_result.setText(m_showText);
            Log.d(TAG, "result="+m_result+",firstNum="+m_firstNum+",operator="+m_operator);

        } else {

            if (m_operator == Operator.EQUAL) {
                m_operator = Operator.NONE;
                m_firstNum = "";
                m_showText = "";
            }
            if (id == R.id.btn_point) {
                input = ".";
            }
            if (m_operator == Operator.NONE) {
                m_firstNum = m_firstNum + input;
            } else {
                m_nextNum = m_nextNum + input;
            }
            m_showText = m_showText + input;
            tv_result.setText(m_showText);
        }

    }


    private enum Operator {
        NONE, ADD, SUB, MULTIPLY, DIVIDE, EQUAL, SQRT
    }

    private Operator m_operator = Operator.NONE;
    private String m_firstNum = "";
    private String m_nextNum = "";
    private String m_result = "";
    private String m_showText = "";

    private boolean calculate() {
        BigDecimal first = new BigDecimal(m_firstNum);
        BigDecimal next = new BigDecimal(m_nextNum);

        switch (m_operator) {
            case ADD:
                first = first.add(next);
                break;
            case SUB:
                first = first.subtract(next);
                break;
            case MULTIPLY:
                first = first.multiply(next);
                break;
            case DIVIDE:
                if (m_nextNum.equals("0")) {
                    Toast.makeText(this, "除数不能为0", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    first = first.divide(next);
                }
                break;
            default:
                return false;
        }

        m_result = String.valueOf(first);
        m_firstNum = m_result;
        m_nextNum = "";
        return true;
    }

    private void clear() {
        m_showText = "";
        tv_result.setText(m_showText);
        m_operator = Operator.NONE;
        m_firstNum = "";
        m_nextNum = "";
        m_result = "";
    }
}
