
function wsconnect() {

    if ("WebSocket" in window) {

        // 打开一个 web socket
        var ws = new WebSocket("ws://localhost:1111/websocket");
        var this_self = this;
        ws.onopen = function () {

            // Web Socket 已连接上，使用 send() 方法发送数据
            // ws.send("发送数据");

            heartCheck.reset();
        };

        ws.onmessage = function (evt) {

            var received_msg = evt.data;
            //接收服务端的新的报修消息，我后端默认返回的是json
            console.log(received_msg);

            if (received_msg == "t1-load") {
                $('#t1').load('real-position-refresh');
            }


        };

        ws.onclose = function () {

            // 关闭 websocket
            console.log("连接已关闭")
        };

        ws.onerror = function () {
            console.log("发生错误");
            ws.close();
            reconnect();
        }


        var heartCheck = {
            timeout: 120000,//120ms
            timeoutObj: null,
            serverTimeoutObj: null,
            reset: function () {
                clearTimeout(this.timeoutObj);
                clearTimeout(this.serverTimeoutObj);
                this.start();
            },
            start: function () {
                var self = this;
                this.timeoutObj = setTimeout(function () {
                    ws.send("HeartBeat");
                    console.log("ping!");

                    self.serverTimeoutObj = setTimeout(function () {
                        ws.close();//如果onclose会执行reconnect，我们执行ws.close()就行了.如果直接执行reconnect 会触发onclose导致重连两次
                        reconnect();
                    }, self.timeout)
                }, this.timeout)
            },
        }

    } else {

        // 浏览器不支持 WebSocket
        alert("您的浏览器不支持 WebSocket!");
    }
}


function reconnect() {

    setTimeout(function () {
        wsconnect();
    }, 3000);
}
