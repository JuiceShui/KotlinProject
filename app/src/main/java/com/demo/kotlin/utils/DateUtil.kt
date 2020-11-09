package com.demo.kotlin.utils

import android.text.format.DateFormat
import android.text.format.DateUtils
import android.text.format.Time
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object DateUtil {
    /**
     * 将时间戳统一转换为13位
     *
     * @param time 10或13位时间戳
     * @return 13位时间戳
     */
    fun getTimeMillis(time: Long): Long {
        return if (Math.abs(time) < 9999999999L) {
            //  转换为13位时间戳
            time * 1000
        } else time
    }

    /**
     * 根据format格式化时间，自动识别10位和13位时间戳
     *
     * @param time   时间戳
     * @param format
     * @return
     */
    fun format(time: Long, format: String?): String {
        var time = time
        time = getTimeMillis(time)
        val df = SimpleDateFormat(format)
        return df.format(Date(time))
    }

    fun format(date: Date?, format: String?): String {
        return SimpleDateFormat(format).format(date)
    }

    fun formatyMdHms(time: String?): String {
        if (time != null && (time.length == 10 || time.length == 13)) {
            val t = time.toLong()
            return format(t, "yyyy-MM-dd HH:mm:ss")
        }
        return ""
    }

    fun formatyMdHms(time: Long): String {
        return format(time, "yyyy-MM-dd HH:mm:ss")
    }

    fun formatyMdHmsCh(time: Long): String {
        val s = formatyMdHms(time)
        return s.substring(0, 4) + "年" + s.substring(5, 7) + "月" + s.substring(
            8,
            10
        ) + "日" + s.substring(10, s.length)
    }

    fun formatyMdHm(time: Long): String {
        return format(time, "yyyy-MM-dd HH:mm")
    }

    fun formatMdHms(time: Long): String {
        return format(time, "MM-dd HH:mm:ss")
    }

    fun formatHmEyMd(time: Long): String {
        return format(time, "HH:mm E yyyy-MM-dd")
    }

    fun formatMdHm(time: Long): String {
        return format(time, "MM-dd HH:mm")
    }

    fun formatMdH(time: Long): String {
        return format(time, "MM-dd HH")
    }

    fun formatyMd(time: Long): String {
        return format(time, "yyyy-MM-dd")
    }

    fun formatyMdCh(time: Long): String {
        val s = formatyMd(time)
        return s.substring(0, 4) + "年" + s.substring(5, 7) + "月" + s.substring(8, 10) + "日"
    }

    fun formatyM(time: Long): String {
        return format(time, "yyyy-MM")
    }

    fun formaty(time: Long): String {
        return format(time, "yyyy")
    }

    fun formatMd(time: Long): String {
        return format(time, "MM-dd")
    }

    fun formatHms(time: Long): String {
        return format(time, "HH:mm:ss")
    }

    fun formatHm(time: Long): String {
        return format(time, "HH:mm")
    }

    fun formatE(time: Long): String {
        return format(time, "E")
    }

    fun getDaysOfWeek(time: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        val dayOfWeek = calendar[Calendar.DAY_OF_WEEK] - 1
        val weekOfDays = arrayOf("Sun", "Mon", "Tues", "Wed", "Thur", "Fri", "Sat")
        return weekOfDays[dayOfWeek]
    }

    fun getDaysOfWeekCH(time: Long): String {
        val calendar = Calendar.getInstance()
        //calendar.setTimeInMillis(time);
        val date = Date(getTimeMillis(time))
        calendar.time = date
        val dayOfWeek = calendar[Calendar.DAY_OF_WEEK] - 1
        val weekOfDays = arrayOf("周日", "周一", "周二", "周三", "周四", "周五", "周六")
        return weekOfDays[dayOfWeek]
    }

    /**
     * string类型转换为date类型
     * strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
     * HH时mm分ss秒，
     * strTime的时间格式必须要与formatType的时间格式相同
     */
    @Throws(ParseException::class)
    fun stringToDate(strTime: String?, formatType: String?): Date? {
        val formatter = SimpleDateFormat(formatType)
        var date: Date? = null
        date = formatter.parse(strTime)
        return date
    }

    /**
     * long转换为Date类型
     * currentTime要转换的long类型的时间
     * formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
     */
    @Throws(ParseException::class)
    fun longToDate(currentTime: Long, formatType: String?): Date? {
        val dateOld = Date(currentTime) // 根据long类型的毫秒数生命一个date类型的时间
        val sDateTime = format(dateOld, formatType) // 把date类型的时间转换为string
        return stringToDate(sDateTime, formatType)
    }

    /**
     * string类型转换为long类型
     * strTime要转换的String类型的时间
     * formatType时间格式
     * strTime的时间格式和formatType的时间格式必须相同
     */
    @Throws(ParseException::class)
    fun stringToLong(strTime: String?, formatType: String?): Long {
        // String类型转成date类型
        val date = stringToDate(strTime, formatType)
        return date?.let {
            // date类型转成long类型
            dateToLong(it)
        } ?: 0
    }

    /**
     * date类型转换为long类型
     * date要转换的date类型的时间
     */
    fun dateToLong(date: Date): Long {
        return date.time
    }

    //时间戳
    val timeStamp: String
        get() {
            val dateFormat = SimpleDateFormat("yyyyMMddHHmmss")
            val date = Date(System.currentTimeMillis())
            return dateFormat.format(date) //时间戳
        }

    fun getTime(time: Long): String {
        var time = time
        val builder: StringBuilder
        builder = StringBuilder()
        val sec = time / (3600 * 1000)
        builder.append(if (sec < 10) "0" else "").append(sec).append(":")
        time = time % (3600 * 1000)
        val min = time / (60 * 1000)
        builder.append(if (min < 10) "0" else "").append(min).append(":")
        time = time % (60 * 1000) / 1000
        builder.append(if (time < 10) "0" else "").append(time)
        return builder.toString()
    }

    /**
     * @param time 当天已过分钟数
     * @return "11:21"
     */
    fun getMinute(time: String): String {
        var t = time.toFloat().toLong()
        t = t * 60000 + 57600000
        return formatHm(Date(t).time)
    }

    /**
     * 时间格式化
     *
     * @param time 连续数字表示的时间 20161023
     * @return -天-小时-分钟
     */
    fun formatDay(time: Long): String {
        return formatyMd(time)
    }

    fun formatDay(time: String): String {
        return formatyMd(time.toLong())
    }

    fun currentTime(): String {
        return (System.currentTimeMillis() / 1000).toString()
    }

    /**
     * @return 年龄
     */
    fun getAge(time: Long): Int {
        val now = formatyMd(System.currentTimeMillis()).split("-").toTypedArray()
        val birthday = formatyMd(time).split("-").toTypedArray()
        var year = now[0].toInt() - birthday[0].toInt()
        val month = now[1].toInt() - birthday[1].toInt()
        val day = now[2].toInt() - birthday[2].toInt()
        if (month < 0 || month == 0 && day < 0) {
            year = year - 1
        }
        return year
    }

    /**
     * 根据时间跨度格式化显示时间
     * 例如,
     * 11:15(今天)
     * 昨天 14:03(昨天)
     * 6月29日 16:12(今年)
     * 2016年6月11日 13:34(更早以前)
     *
     * @param time 需要格式化显示的过去时间点
     * @return 格式化显示的时间
     */
    fun getRelativeTimeSpanString(time: Long): String {
        var time = time
        time = getTimeMillis(time)
        val isToday = DateUtils.isToday(time)
        if (isToday) {
            // 今天
            return DateFormat.format("HH:mm", time).toString()
        }
        val isYesterday = DateUtils.isToday(time - 86400000)
        if (isYesterday) {
            // 昨天
            return "昨天 " + DateFormat.format("HH:mm", time).toString()
        }
        val theTime = Time()
        theTime.set(time)
        val thenYear = theTime.year
        theTime.set(System.currentTimeMillis())
        return if (thenYear == theTime.year) {
            // 今年
            DateFormat.format("MM月dd日 HH:mm", time).toString()
        } else DateFormat.format("yyyy年MM月dd日 HH:mm", time).toString()
    }
}