<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta charset="UTF-8">
<title>Demo</title>
<!-- 引入echarts文件 -->
<script src="plugins/echarts/echarts.js"></script>
<script src="js/jquery.min.js?v=2.1.4"></script>
</head>
<body>
    <!-- 为ECharts准备一个具备大小（宽高）的Dom -->
    <div id="main" style="width: 600px; height: 400px;"></div>
    <script type="text/javascript">
        alert("1.准备初始化echarts实例");
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('main'));

        // 指定图表的配置项和数据
        var option = {
        	    backgroundColor: '#fff',

        	    title: {
        	        text: '课堂类别',
        	        left: 'center',
        	        top: 20,
        	        textStyle: {
        	            color: '#ccc'
        	        }
        	    },

        	    tooltip : {
        	        trigger: 'item',
        	        formatter: "{a} <br/>{b} : {c} ({d}%)"
        	    },

        	    visualMap: {
        	        show: false,
        	        min: 80,
        	        max: 600,
        	        inRange: {
        	            colorLightness: [0, 1]
        	        }
        	    },
        	    series : [
        	        {
        	            name:'访问来源',
        	            type:'pie',
        	            radius : '55%',
        	            center: ['50%', '50%'],
        	            data:[
        	                {value:330, name:'创新创业'},
        	                {value:350, name:'电商综合'},
        	                {value:360, name:'平台实操'},
        	                {value:278, name:'网络营销'},
        	                {value:400, name:'应用技能'}
        	            ].sort(function (a, b) { return a.value - b.value; }),
        	            roseType: 'radius',
        	            label: {
        	                normal: {
        	                    textStyle: {
        	                        color: 'rgba(0, 0, 0, 0.3)'
        	                    }
        	                }
        	            },
        	            labelLine: {
        	                normal: {
        	                    lineStyle: {
        	                        color: 'rgba(0, 0, 0, 0.3)'
        	                    },
        	                    smooth: 0.2,
        	                    length: 10,
        	                    length2: 20
        	                }
        	            },
        	            itemStyle: {
        	                normal: {
        	                    color: '#c23531',
        	                    shadowBlur: 200,
        	                    shadowColor: 'rgba(255, 255, 255, 0.5)'
        	                }
        	            },

        	            animationType: 'scale',
        	            animationEasing: 'elasticOut',
        	            animationDelay: function (idx) {
        	                return Math.random() * 200;
        	            }
        	        }
        	    ]
        	};

        alert("2.准备指定配置项");
        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);

        // 相当于 document.ready，{代码}
        $(function(){
            alert("3.页面加载完毕");
        })
    </script>
</body>
</html>