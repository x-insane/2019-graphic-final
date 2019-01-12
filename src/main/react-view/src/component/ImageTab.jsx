import React from 'react';

const ImageTab = props => {
    return <div className="image-handler">
        <div className="source">
            <p>原图</p>
            <img src={"/source/" + props.filename} alt={props.filename}/>
        </div>
        <div className="emboss">
            <p>浮雕</p>
            <img src={"/emboss/" + props.filename} alt="图像浮雕"/>
        </div>
        <div className="paint">
            <p>油画</p>
            <img src={"/paint/" + props.filename} alt="图像油画"/>
        </div>
    </div>
};

export default ImageTab;