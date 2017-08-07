requirejs.config({
            paths: {
                'jquery': 'js/jquery',
                'underscore': 'http://cdnjs.cloudflare.com/ajax/libs/underscore.js/1.3.3/underscore-min',
                'bootstrap': 'js/bootstrap/js',
                'fuelux': 'src'
            }
        });

        require(['jquery', 'sample/data', 'sample/datasource', 'sample/datasourceTree', 'fuelux/all'], function ($, sampleData, StaticDataSource, DataSourceTree) {

            // WIZARD
            $('#MyWizard').on('change', function(e, data) {
                console.log('change');
                if(data.step===3 && data.direction==='next') {
                    // return e.preventDefault();
                }
            });
            $('#MyWizard').on('changed', function(e, data) {
                console.log('changed');
            });
            $('#MyWizard').on('finished', function(e, data) {
                console.log('finished');
            });
            $('#btnWizardPrev').on('click', function() {
                $('#MyWizard').wizard('previous');
            });
            $('#btnWizardNext').on('click', function() {
                $('#MyWizard').wizard('next','foo');
            });
            $('#btnWizardStep').on('click', function() {
                var item = $('#MyWizard').wizard('selectedItem');
                console.log(item.step);
            });
            $('#MyWizard').on('stepclick', function(e, data) {
                console.log('step' + data.step + ' clicked');
                if(data.step===1) {
                    // return e.preventDefault();
                }
            });

        });