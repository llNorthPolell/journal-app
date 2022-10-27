import {useState, useEffect} from 'react'



const useSimpleForm = (initData,submitEndpointFN) => {
    const [fieldStates, setFieldStates] = useState(initData);

    useEffect(()=>{
        setFieldStates(initData);
        console.log("Init Data = " + JSON.stringify(initData));
    },[initData])

    const compileOutput = ()=>{
        let output = {...fieldStates};
        Object.keys(output).forEach(k=> 
            output[k] = typeof output[k] =='string'? 
                output[k].trim():fieldStates[k]
        );
        output.lastUpdated = new Date().toISOString();
        return output;
    }

    const reset = () => {
        setFieldStates({...initData});
    }

    const update = (delta) => {
        setFieldStates({...fieldStates,...delta});
    }
    const submit = overrideOutput => {
        const output = overrideOutput? overrideOutput : compileOutput();
        console.log("Save to database: " + JSON.stringify(output));

        if (submitEndpointFN)
            submitEndpointFN(output);
    }
 
    return [fieldStates, update, submit, reset];
}

export default useSimpleForm;
