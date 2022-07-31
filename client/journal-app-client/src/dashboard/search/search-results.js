import {Link, useParams} from 'react-router-dom';

function SearchResults(props){
    const {journalId} = useParams();

    return (
        <div id="searchResultsDiv">
            {
                props.searchResults.map(searchResult=>
                    <div className="card">
                        <div className="card-header">
                            <div className="row">
                                <div className="col-md-10">
                                    <h5>{searchResult.topic}</h5>
                                </div>
                                <div className="col-md-2">
                                    <p>{searchResult.dateOfEntry}</p>
                                </div>
                            </div>
                        </div>
                        <div className="card-body">
                            <div className="card-text"> 
                                {searchResult.description}
                                <Link className="stretched-link" to={"/"+journalId+"/"+searchResult.key}></Link>
                            </div>
                        </div>
                    </div>
                )
            }
        </div>
    );
}
export default SearchResults;