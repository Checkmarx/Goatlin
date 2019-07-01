const Account = require('../models/account');

module.exports = async function (req, res, next) {
    const header = req.get('Authorization') || '';

    if (header === '') {
        return res.status(401).end()
    }

    try {
        const [scheme, data] = header.split(' ');
        const [email, password] = Buffer.from(data, 'base64')
            .toString()
            .split(':')

        const account = await Account.findOne({email,password}).exec();
        if (account === null) {
            return res.status(401).end()
        }

        return next();
    } catch (e) {
        let status = 500;
        let error = 'Authentication failed'

        console.error(e);

        res.status(status).json({error}).end();
    }
};
