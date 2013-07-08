module.exports = function (grunt) {
// Project configuration.
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        copy: {
            main: {
                files: [
                    {src: ['assets/**'], dest: 'dist/'} // includes files in path
               ]
            }
        },
        cssmin: {

                minify: {
                    expand: true,
                    cwd: 'assets/css/',
                    src: ['*.css', '!*.min.css','template/*.css','global/*.css'],
                    dest: 'dist/assets/css/',
                    ext: '.css'
                }

        },

        transport: {
            options: {
                paths:['.'],
                alias : '<%= pkg.spm.alias %>'
            },
            //模块整理
            init : {
                options : {
                    idleading : 'dist/init/'
                }
                ,files : [
                    {
                        cwd : 'assets/js/front/'
                        ,src : '**/*'
                        ,filter : 'isFile'
                        ,dest : 'dist/assets/js/'
                    }
                ]
            }
        }


   //     uglify: {
   //         options: {
   //             mangle: false
   //         },
   //         my_target: {
    //            files: [
   //                 {
    //                    cwd: 'src',
   //                     src: '**/*',
  //                      dest: 'dist'
    //                }
    //            ]
   //         }
   //     }



    });


    // Load the plugin that provides the "uglify" task.

    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-contrib-cssmin');
     grunt.loadNpmTasks('grunt-cmd-transport');
   grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-clean')

    grunt.registerTask('build', ['copy', 'cssmin','transport']);

};