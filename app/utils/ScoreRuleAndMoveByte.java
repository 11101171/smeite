package utils;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-28
 * Time: 上午11:25
 * To change this template use File | Settings | File Templates.
 */


import java.math.BigInteger;

/**
 * Created with IntelliJ IDEA.
 * User: cutter.li
 * Date: 13-1-18
 * Time: 下午5:24
 * 移位和积分规则类
 */
public class ScoreRuleAndMoveByte {

    public static Long moveByte(long oldHistory,long moveAmonut)
    {
        long moveResult= oldHistory<<moveAmonut;
        long result=  Long.parseLong(toFullBinaryString(moveResult),2)+1;
        return result;
    }


    /**
     * 读取
     * @param num
     * @return
     */
    private static String toFullBinaryString(long num) {
        final int size=42;
        char[] chs = new char[size];
        for(int i = 0; i < size; i++) {
            chs[size - 1 - i] = (char)(((num >> i) & 1) + '0');
        }
        return new String(chs);
    }

    /**
     * 按照积分规则，得到积分 ,
     * 积分规则如下：
     签到功能说明
     .每天只能签到一次（按服务器系统时间为准）
     .连续签到 额外奖励积分，同种礼包只能使用一次
     .连续签到10天，一次性奖励2积分
     .连续签到30天，一次性奖励10积分
     .连续签到60天，一次性奖励30积分
     .连续签到90天，一次性奖励100积分
     * @param signCount  连续签到次数
     * @return 增加的积分
     */
    public static  int getScoreByRule(int signCount)
    {
        int addScore=1;

        if(signCount==10)
        {
            addScore+=2;
        }
        else if(signCount==30)
        {
            addScore+=10;
        }
        else if(signCount==60)
        {
            addScore+=30;
        }
        else if(signCount==90)
        {
            addScore+=100;
        }

        return addScore;
    }



    public static  void main(String[] args)
    {
        long result= moveByte(1,3);

        System.out.println("移位结果："+result);

        System.out.println("连续签到次数9：所增加的积分："+getScoreByRule(9));
        System.out.println("连续签到次数10：所增加的积分："+getScoreByRule(10));
        System.out.println("连续签到次数29：所增加的积分："+getScoreByRule(29));
        System.out.println("连续签到次数30：所增加的积分："+getScoreByRule(30));
        System.out.println("连续签到次数59：所增加的积分："+getScoreByRule(59));
        System.out.println("连续签到次数60：所增加的积分："+getScoreByRule(60));
        System.out.println("连续签到次数89：所增加的积分："+getScoreByRule(89));
        System.out.println("连续签到次数90：所增加的积分："+getScoreByRule(90));
        System.out.println("连续签到次数91：所增加的积分："+getScoreByRule(91));
    }







}