
function TopicField(props){
    return (
        (props.mode=="NEW")?
            <div className="row mb-3">
                <div className="col">
                    <select id="topicDropdown" className="form-select" name="topic" value={props.topic} onChange={props.handleChangeTopic}>
                        <option value="">New Topic</option>
                        {
                            props.topicList.map(topicListItem => (
                                <option value={topicListItem}>{topicListItem}</option>
                            ))
                        }
                    </select>
                </div>
                <div className="col">
                    {
                    (props.topic=="")?
                        <input id="newTopicField" className="form-control" name="newTopic" value={props.newTopic} onChange={props.handleChangeNewTopic} placeholder="New Topic Name"/>
                        :
                        null
                    }
                </div>
            </div>
        :
            <h5 className="card-title">{props.topic}</h5>
    );
}
export default TopicField;