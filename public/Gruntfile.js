module.exports = function (grunt) {
// Project configuration.
    grunt.initConfig({
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

        }
   //     },


   //     transport: {
  //          target_name: {
   ///             files: [
   //                 {
   //                     cwd: 'js/front',
   //                     src: '**/*',
    //                    dest: 'dist/assets'
    //                }
   //             ]
   //         }
   //     },


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
  //  grunt.loadNpmTasks('grunt-cmd-transport');
 //   grunt.loadNpmTasks('grunt-contrib-uglify');
 //   grunt.loadNpmTasks('grunt-contrib-clean')

    grunt.registerTask('build', ['copy', 'cssmin']);

};