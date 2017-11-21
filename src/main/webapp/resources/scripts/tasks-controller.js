tasksController = function () {

    function errorLogger(errorCode, errorMessage) {
        console.log(errorCode + ':' + errorMessage);
    }

    var taskPage;
    var initialised = false;

    /**
     * makes json call to server to get task list.
     * currently just testing this and writing return value out to console
     * 111917kl
     */

    /**
     * 111917kl
     * callback for retrieveTasksServer
     * @param data
     */
    function displayTasksServer(data) { //this needs to be bound to the tasksController -- used bind in retrieveTasksServer 111917kl
        console.log(data);
        tasksController.loadServerTasks(data);
    }

    function taskCountChanged() {
        var count = $(taskPage).find('#tblTasks tbody tr').length;
        $('footer').find('#taskCount').text(count);
    }

    function clearTask() {
        $(taskPage).find('form').fromObject({});
    }

    function renderTable() {
        $.each($(taskPage).find('#tblTasks tbody tr'), function (idx, row) {
            var due = Date.parse($(row).find('[datetime]').text());
            if (due.compareTo(Date.today()) < 0) {
                $(row).addClass("overdue");
            } else if (due.compareTo((2).days().fromNow()) <= 0) {
                $(row).addClass("warning");
            }
        });
    }

    return {
        init: function (page, callback) {
            if (initialised) {
                callback()
            } else {
                taskPage = page;
                storageEngine.init(function () {
                    storageEngine.initObjectStore('task', function () {
                        callback();
                    }, errorLogger)
                }, errorLogger);
                $(taskPage).find('[required="required"]').prev('label').append('<span>*</span>').children('span').addClass('required');
                $(taskPage).find('tbody tr:even').addClass('even');

                $(taskPage).find('#btnAddTask').click(function (evt) {
                    evt.preventDefault();
                    // language=JQuery-CSS
                    $(taskPage).find('#taskCreation').removeClass('not');
                });

                $(taskPage).find('#bthAddTeam').click(function (evt) {
                    evt.preventDefault();
                    $(taskPage).find('#taskTeam').removeClass('not');
                });


                /**     * 11/19/17kl        */
                $(taskPage).find('#btnRetrieveTasks').click(function (evt) {
                    evt.preventDefault();
                    console.log('making ajax call');
                    /*retrieveTasksServer();*/
                });

                $(taskPage).find('#btnFilterTask').click(function (evt) {
                    evt.preventDefault();
                    console.log('making ajax call');
                    /*retrieveTasksServer();*/
                    tasksController.retrieveTeams();
                });

                $(taskPage).find('#tblTasks tbody').on('click', 'tr', function (evt) {
                    $(evt.target).closest('td').siblings().andSelf().toggleClass('rowHighlight');
                });

                $(taskPage).find('#tblTasks tbody').on('click', '.deleteRow',
                    function (evt) {
                        storageEngine.delete('task', $(evt.target).data().taskId,
                            function () {
                                $(evt.target).parents('tr').remove();
                                taskCountChanged();
                            }, errorLogger);

                    }
                );

                $(taskPage).find('#tblTasks tbody').on('click', '.editRow',
                    function (evt) {
                        $(taskPage).find('#taskCreation').removeClass('not');
                        storageEngine.findById('task', $(evt.target).data().taskId, function (task) {
                            $(taskPage).find('form').fromObject(task);
                        }, errorLogger);
                    }
                );

                $(taskPage).find('#clearTask').click(function (evt) {
                    evt.preventDefault();
                    clearTask();
                });

                $(taskPage).find('#tblTasks tbody').on('click', '.completeRow', function (evt) {
                    storageEngine.findById('task', $(evt.target).data().taskId, function (task) {
                        task.complete = true;
                        storageEngine.save('task', task, function () {
                            tasksController.loadTasks();
                        }, errorLogger);
                    }, errorLogger);
                });

                $(taskPage).find('#saveTask').click(function (evt) {
                    evt.preventDefault();

                    if ($(taskPage).find('form').valid()) {
                        var task = $(taskPage).find('form').toObject();
                        storageEngine.save('task', task, function () {
                            $(taskPage).find('#tblTasks tbody').empty();
                            /*tasksController.loadTasks();*/
                            tasksController.saveTask();
                            clearTask();
                            $(taskPage).find('#taskCreation').addClass('not');
                        }, errorLogger);
                    }
                });

                $(taskPage).find('#saveTeam').click(function (evt) {
                    evt.preventDefault();

                    if ($(taskPage).find('form').valid()) {
                        var task = $(taskPage).find('form').toObject();
                        storageEngine.save('task', task, function () {
                            $(taskPage).find('#tblTeam tbody').empty();
                            /*tasksController.loadTasks();*/
                            tasksController.saveTeam();
                            clearTask();
                            $(taskPage).find('#taskTeam').addClass('not');
                        }, errorLogger);
                    }
                });

                $(taskPage).find('#btnName').click(function (evt) {
                    evt.preventDefault();

                    tasksController.retriveSortTask();
                });

                $(taskPage).find('#btnTeamName').click(function (evt) {
                    evt.preventDefault();

                    if ($(taskPage).find('form').valid()) {
                        var task = $(taskPage).find('form').toObject();
                        storageEngine.save('task', task, function () {
                            $(taskPage).find('#tblTeam tbody').empty();
                            /*tasksController.loadTasks();*/
                            tasksController.retrieveTasksServer();
                            clearTask();
                            $(taskPage).find('#taskTeam').addClass('not');
                        }, errorLogger);
                    }
                });
                initialised = true;
            }
        },
        /**
         * 111917kl
         * modification of the loadTasks method to load tasks retrieved from the server
         */
        loadServerTasks: function (tasks) {
            $(taskPage).find('#tblTasks tbody').empty();
            $.each(tasks, function (index, task) {
                if (!task.complete) {
                    task.complete = false;
                }
                $('#taskRow').tmpl(task).appendTo($(taskPage).find('#tblTasks tbody'));
                taskCountChanged();
                console.log('about to render table with server tasks');
                //renderTable(); --skip for now, this just sets style class for overdue tasks 111917kl
            });
        },
        loadTasks: function () {
            $(taskPage).find('#tblTasks tbody').empty();
            storageEngine.findAll('task', function (tasks) {
                tasks.sort(function (o1, o2) {
                    return Date.parse(o1.requiredBy).compareTo(Date.parse(o2.requiredBy));
                });
                $.each(tasks, function (index, task) {
                    if (!task.complete) {
                        task.complete = false;
                    }
                    $('#taskRow').tmpl(task).appendTo($(taskPage).find('#tblTasks tbody'));
                    taskCountChanged();
                    renderTable();
                });
            }, errorLogger);
        },

        saveTask: function () {
            $.ajax("TaskServlet", {
                "type": "post",
                //dataType: "json"
                "data": {
                    "task": $("#task").val(),
                    "userId": $("#userId").val(),
                    "requiredBy": $("#requiredBy").val(),
                    "priority": $("#priority").val(),
                    "category": $("#category").val()


                }
            }).done(displayTasksServer.bind()); //need reference to the tasksController object
        },

        saveTeam: function () {
            $.ajax("TeamServlet", {
                "type": "post",
                "data": {
                    "name": $("#txtTeam").val(),
                    "description": $("#txtDescription").val(),
                }
            }).done(displayTasksServer.bind()); //need reference to the tasksController object
        },
        retrieveTasksServer: function () {
            $.ajax("TaskServlet", {
                "type": "get",
                dataType: "json"
            }).done(displayTasksServer.bind()); //need reference to the tasksController object
        },
        retriveSortTask: function () {
            $.ajax("TaskServlet", {
                "type": "get",
                dataType: "json",
                "data": {
                    "sort": $("#btnName").val(),

                }
            }).done(displayTasksServer.bind()); //need reference to the tasksController object
        },

        retrieveTeams: function () {
            $.ajax("TeamServlet", {
                "type": "get",
                dataType: "json"
            }).done(displayTasksServer.bind()); //need reference to the tasksController object
        }


    }

}();
