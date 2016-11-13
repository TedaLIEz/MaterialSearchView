## My implementation of SearchView in Material Design

## Screenshots

<img src="art/screenshots/searchview_1.png" alt="view_1" width="360" height="640"/>

<img src="art/screenshots/searchview_2.png" alt="view_2" width="360" height="640"/>

## Usage

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        mSearchView.setMenuItem(item);
        return true;
    }
    
    mSearchView.setOnQueryListener(new MaterialSearchView.OnTextQueryListener() {
                @Override
                public boolean onQueryTextSubmit(CharSequence query) {
                    return true;
                }
    
                @Override
                public boolean onQueryTextChange(CharSequence newText) {
                    return true;
                }
            });

Feel free to send me issues and **pull requests**

## TODO list:
* Add unit tests & integration tests
* Add more custom styles
* fix issues :)

## Known issues:
* IllegalArgumentException when View.onRestoreInstanceState is called

## License

    Copyright 2016 TedaLIEz

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
