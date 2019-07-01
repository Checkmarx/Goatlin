const mongoose = require('mongoose');

const noteSchema = new mongoose.Schema({
    id: {
        type: String,
        required: true
    },
    owner: {
        type: String,
        required: true
    },
    title: {
        type: String,
        required: true,
    },
    content: {
        type: String,
        required: true
    },
    createdAt: {
        type: Date,
        default: Date.now()
    }
});

module.exports = new mongoose.model('Note', noteSchema);
