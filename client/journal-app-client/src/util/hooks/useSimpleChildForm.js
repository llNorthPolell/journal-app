import {useState} from 'react'



const useSimpleChildForm = (initData,updateParentFN) => {
    const [fieldStates, setFieldStates] = useState(initData);

    const reset = () => {
        setFieldStates({...initData});
    }

    const update = (delta) => {
        setFieldStates({...fieldStates,...delta});
    }

    const updateParent = e => {
        e.preventDefault();
        const output = {...fieldStates};
        Object.keys(output).forEach(k=> 
            output[k] = typeof output[k] =='string'? 
                output[k].trim():fieldStates[k]
        );

        if (updateParentFN)
            return updateParentFN(output);
    }
    
    return [fieldStates, update, updateParent, reset];
}

export default useSimpleChildForm;
