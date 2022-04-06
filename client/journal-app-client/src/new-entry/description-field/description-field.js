import SimpleInput from "../../util/components/simple-input";


function DescriptionField(props){
    return (
        (props.mode=="VIEW")?
            <p className="card-text">{props.description}</p>
        :
            <SimpleInput 
                id="descriptionField" 
                value={props.description} 
                displayName = "Description"
                fieldName="description" 
                type="textarea" 
                handleUpdate={props.handleChangeDescription}></SimpleInput>  
    );
}
export default DescriptionField;