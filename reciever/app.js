console.log('XXXXXXXxx');

        var self = this;
        self.receiverWrapper = new ReceiverManagerWrapper("~reversi");
        var messageBus = self.receiverWrapper.createMessageBus("urn:flint:org.openflint.fling.reversi");

        messageBus.on("message", function (senderId, data) {
            console.log('********onMessage********' + data);
            var message = JSON.parse(data);
            console.log(' message command: ' + message.command);
            if (message.command === 'start') {
                self.speedY = speedY + Math.random() * 10 + 100;
                self.speedX = speedX + Math.random() * 10 + 100;
                startMove(oContainer);
            } else if (message.command === 'stop') {
            } else {
                console.log('Invalid message command: ' + message.command);
            }
        });

        self.receiverWrapper.open();