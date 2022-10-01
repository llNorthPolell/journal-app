import {Link, useParams} from 'react-router-dom';

function JournalEntryCard(props){
    const {journalId} = useParams();

    return (
        <div id="journalEntryCardDiv">
            {
                <div className="card">
                    <div className="card-header">
                        <div className="row">
                            <div className="col-md-10">
                                <h5>{props.entry.summary}</h5>
                            </div>
                            <div className="col-md-2">
                                <p>{props.entry.dateOfEntry}</p>
                            </div>
                        </div>
                    </div>
                    <div className="card-body">
                        <div className="card-text">
                            {props.entry.overview}
                            <Link className="stretched-link" to={"/" + journalId + "/" + props.entry.key}></Link>
                        </div>
                    </div>
                </div>
            }
        </div>
    );
}
export default JournalEntryCard;