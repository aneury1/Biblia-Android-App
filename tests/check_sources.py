"""Offline structural checks; Gradle tests remain the functional Android checks."""
from pathlib import Path
import re
import xml.etree.ElementTree as ET
import zipfile
root = Path(__file__).resolve().parents[1]
resources = ET.parse(root / 'app/src/main/res/values/strings.xml').getroot()
names = {node.attrib['name'] for node in resources if node.tag == 'string'}
for file in (root / 'app/src/main/java/com/aneury1/biblia/reader').glob('*.kt'):
    missing = set(re.findall(r'R\.string\.(\w+)', file.read_text())) - names
    assert not missing, (file, missing)
for file in (root / 'app/src/main/res').rglob('*.xml'):
    ET.parse(file)
with zipfile.ZipFile(root / 'gradle/wrapper/gradle-wrapper.jar') as wrapper:
    assert wrapper.testzip() is None
    assert 'org/gradle/wrapper/GradleWrapperMain.class' in wrapper.namelist()
print('PASS: string references, resource XML and Gradle wrapper integrity')
