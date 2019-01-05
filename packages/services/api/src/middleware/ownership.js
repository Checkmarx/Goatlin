/**
 * Authorization middleware responsible to verify authentication user ownership
 * over request resource
 * @param  {Request}    req     Request object
 * @param  {Response}   res     Response object
 * @param  {Function}   next    Next middleware
 * @return {void}
 */
function ownership (req, res, next) {
    if (req.params.username !== req.account.username) {
        res.statusMessage = "Unauthorized"
        return res.status(403).end();
    }

    next();
}

module.exports = ownership
