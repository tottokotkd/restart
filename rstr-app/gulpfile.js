'use strict';

var gulp        = require('gulp'),
    typescript  = require('typescript'),
    ts          = require('gulp-typescript');
    // browserify  = require('browserify'),
    // source      = require('vinyl-source-stream'),
    // del         = require('del'),
    // path        = require("path"),
    // babelify    = require('babelify'),
    // glob        = require("glob"),
var mkdirp      = require("mkdirp");
    // factor      = require('factor-bundle'),
    // fs          = require('fs'),
var runSequence = require('run-sequence');
var webpack = require('gulp-webpack');


var destination = './dist'
var tmpCompileDir = './.tmp/js'

gulp.task('default', function (callback) {
    runSequence('static-resource', 'ts-main', callback);
});


/*
 * static resources
 * */
//
gulp.task('static-resource', function (callback) {
    mkdirp.sync(destination);
    return runSequence('static-resource-html', callback);
});

// html
gulp.task('static-resource-html', function () {
    return gulp.src('./html/*').pipe(gulp.dest(destination));
});
//
// // js library
// gulp.task('static-resource-js', function () {
//     var dir = path.join(destination, 'js');
//     mkdirp.sync(dir);
//     return gulp.src('src/js/*').pipe(gulp.dest(dir));
// });
//
// // aws cognito library
// gulp.task('static-resource-awscognito', function () {
//     var dir = path.join(destination, 'js');
//     mkdirp.sync(dir);
//
//     gulp.src('node_modules/amazon-cognito-identity-js/dist/*').pipe(gulp.dest(dir));
//     gulp.src('node_modules/moment/min/moment.min.js').pipe(gulp.dest(dir));
//     return gulp.src('node_modules/sjcl/sjcl.js').pipe(gulp.dest(dir));
//
// });
//
// // css
// gulp.task('static-resource-css', function () {
//     var dir = path.join(destination, 'css');
//     mkdirp.sync(dir);
//     return gulp.src('src/css/*').pipe(gulp.dest(dir));
// });

/*
 * typescript
 * */

var project = ts.createProject('tsconfig.json', {typescript: typescript});

// main modules
gulp.task('ts-main', function (callback) {
    runSequence('ts-compile', 'webpack', callback);
});

gulp.task('ts-compile', function () {
    mkdirp.sync(tmpCompileDir);
    return gulp.src(["./src/**/*"])
        .pipe(ts(project)).js
        .pipe(gulp.dest(tmpCompileDir));
});


gulp.task('webpack',function(){
    var webpackConfig = require('./webpack.config.js');
    gulp.src([tmpCompileDir + '/**/*'])
        .pipe(webpack(webpackConfig))
        .pipe(gulp.dest(destination + '/js'))
})
//
// gulp.task('ts-main-bundle', function () {
//     var entries = ".tmp/root/*";
//     var outputDir = "dist/js";
//     var files = glob.sync(entries, {nodir: true});
//     var outputs = files.map(function(f) { return f.replace(".tmp/root", outputDir) });
//     mkdirp.sync(outputDir);
//     return browserify({entries: files, extensions: ['js', 'jsx']})
//         .plugin('factor-bundle', { output: outputs })
//         .bundle()
//         .pipe(source("common.js"))
//         .pipe(gulp.dest(outputDir));
// });
// //
// gulp.task('ts-main-cleanup', function () {
//     return del(['.tmp']);
// });
