const createError = require('http-errors');
const express = require('express');
const path = require('path');
const logger = require('morgan');
const database = require('./lib/database');
const config = require('../config.json');

const indexRouter = require('./routes/index');
const accountsRouter = require('./routes/accounts');

const app = express();

app.use(logger('dev'));
app.use(express.json());

app.use('/', indexRouter);
app.use('/', accountsRouter);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.end();
});

// establish database connection
database.connect(config.database);

module.exports = app;
