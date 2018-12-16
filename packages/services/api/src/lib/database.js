const mongoose = require('mongoose');

function connect(connectionString) {
    mongoose.connect(connectionString, { useNewUrlParser: true });

    mongoose.connection.on('error', console.error.bind(console, 'connection error:'));
    mongoose.connection.once('open', () => {
        console.log('Database connection established!');
    });
}

module.exports = { connect };
