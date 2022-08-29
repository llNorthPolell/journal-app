import {useState} from 'react'



const useSimpleForm = (initData,submitEndpointFN) => {
    const [fieldStates, setFieldStates] = useState(initData);
    const compileOutput = ()=>{
        let output = {...fieldStates};
        output.lastUpdated = new Date().toISOString();
        return output;
    }

    const reset = () => {
        setFieldStates({...initData});
    }

    const update = (delta) => {
        setFieldStates({...fieldStates,...delta});
    }
    const submit = () => {
        const output = compileOutput();
        console.log("Save to database: " + JSON.stringify(output));

        if (submitEndpointFN)
            submitEndpointFN(output);
    }
 
    return [fieldStates, update, submit, reset];
}

export default useSimpleForm;
