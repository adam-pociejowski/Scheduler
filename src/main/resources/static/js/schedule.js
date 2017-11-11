function viewModel() {
    var self = this;
    self.showTable = ko.observable(false);
    self.response = ko.observable({});
    self.minHour = 7;
    self.maxHour = 20;
    self.scheduleData = {
        inputFileName: 'myPlan.csv',
        populationSize: 30,
        eliteChromosomesNumber: 6,
        tournamentSelectionNumber: 8,
        mutationRate: 0.05,
        maxIterations: 10000,
        daysAmount: 2,
        hoursAmount: (self.maxHour - self.minHour + 1)
    };

    this.generateSchedule = function () {
        $.ajax({
            type: 'POST',
            url: 'rest/schedule',
            data: JSON.stringify(self.scheduleData),
            contentType: 'application/json',
            success: function (response) {
                self.showTable(true);
                self.response(response);
                self.generateTable(response);
            },
            error: function (error) {
                console.log(error);
            }
        })
    };

    self.generateTable = function (data) {
        var slots = data.slots;
        var roomsAmount = data.roomsAmount;
        $('#scheduleTable').html('');
        for (var i = roomsAmount; i > 0; i--) {
            appendRow(i);
        }
        appendHoursAndDays();
        appendSchedule(slots, roomsAmount);
    };

    self.appendSchedule = function(slots, roomsAmount) {
       for (var i = 0; i < slots.length; i++) {
           var slot = slots[i];
           if (slot.length > 1) {
               console.error('More than one classes for slot '+i);
           } else if (slot.length === 1) {
               appendClassToCells(slot[0], roomsAmount, i);
           }
       }
    };

    self.appendClassToCells = function(clazz, roomsAmount, index) {
        var startHour = parseInt(index / roomsAmount) + 1;
        var room = (index % roomsAmount) + 1;
        var color = this.generateColor();

        for (var i = 0; i < clazz.duration; i++) {
            var hourIndex = startHour + i;
            $('#'+hourIndex+'_'+room)
                .append('<div>'+clazz.course.name+'</div>'+
                    '<div>'+clazz.professor.name+'</div>'+
                    '<div>'+clazz.studentGroup.name+'</div>')
                .css('background', color);
        }
    };

    self.generateColor = function() {
        var maxBrightness = 100;
        var red = Math.floor(Math.random() * 130) + maxBrightness;
        var green = Math.floor(Math.random() * 130) + maxBrightness;
        var blue = Math.floor(Math.random() * 130) + maxBrightness;
        return 'rgb('+red+','+green+','+blue+')';
    };

    self.appendRow = function(roomNumber) {
        $('#scheduleTable').append('<tr id=\"'+roomNumber+'\"></tr>');
        $('#'+roomNumber)
            .append(self.appendRoomCell(roomNumber))
            .append(self.appendCells(roomNumber));
    };

    self.appendHoursAndDays = function() {
        $('#scheduleTable').append('<tr id="hours"></tr>');
        var content = '<td></td>';
        for (var i = 0; i < scheduleData.daysAmount; i++) {
            for (var j = minHour; j <= maxHour; j++) {
                content += '<td class="hourCell">'+j+'</td>';
            }
        }
        $('#hours').append(content);

        $('#scheduleTable').append('<tr id="days"></tr>');
        var daysContent = '<td></td>';
        for (var i = 0; i < scheduleData.daysAmount; i++) {
            daysContent += '<td class="dayCell" colspan="'+scheduleData.hoursAmount+'">Day '+i+'</td>';
        }
        $('#days').append(daysContent);
    };

    self.appendRoomCell = function(roomNumber) {
        return '<td id="0_'+roomNumber+'" class="roomCell">Room '+roomNumber+'</td>';
    };

    self.appendCells = function(roomNumber) {
        var cells = '';
        for (var i = 0; i < self.scheduleData.daysAmount * self.scheduleData.hoursAmount; i++) {
            cells += appendCell(roomNumber, i + 1);
        }
        return cells;
    };

    self.appendCell = function(roomNumber, columnNumber) {
        return '<td id="'+columnNumber+'_'+roomNumber+'" class="cell"></td>';
    };
}

ko.applyBindings(viewModel);