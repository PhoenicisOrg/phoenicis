For the ui design, the QtDesigner that comes bundles with QtJambi (IT NEEDS TO BE THE ONE THAT IS BUNDLED WITH QTJAMBI)
was used. The so-generated jui-file was then compiled with juic (jui compiler, a tool that comes bundles with QtJambi aswell)
The so-generated file (UI_<WINDOW_NAME>) needs some adjustments for POL:

- Due to a bug, themed icons don't work at the moment, so those need to be manually set through:
	<WIDGET/ACTION>.setIcon(QIcon.fromTheme( <ICON_NAME> ));

- To use the POL-i18n instead of the one that Qt provides, all calls to QApplication.translate() within the method
	"retranslateUi()" of the generated java-file have to be replaced with the following pattern:
		'QCoreApplication.translate("<WINDOW_NAME>",  "<STRING>", null)'  =>  'translate("<STRING>")'

- If icons from the filesystem were used, the IconHelper should be used to load those resources into QIcons:
	IconHelper.fromResource(getClass(), "<RELATIVE_RESOURCE_PATH>")