function SearchResults(props){
    return (
        <div id="searchResultsDiv">
            {
                props.searchResults.map(searchResult=>
                    <div className="card">
                        <div className="card-header">
                            {searchResult.topic}
                        </div>
                        <div className="card-body">
                            <div className="card-text"> 
                                {searchResult.description}
                            </div>
                        </div>
                    </div>
                )
            }
        </div>
    );
}
export default SearchResults;