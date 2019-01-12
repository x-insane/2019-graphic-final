import React from 'react';
import { Upload, Icon, message } from 'antd';

const Dragger = Upload.Dragger;

class MainTab extends React.Component {

    state = {
        images: []
    };

    componentDidMount() {
        if (window.sessionStorage) {
            const images = window.sessionStorage.getItem("images");
            if (images)
                window.images = JSON.parse(images);
        }
        this.setState({
            images: window.images || []
        });
        window.addEventListener('resize', this.onResize);
    }

    componentWillUnmount() {
        window.removeEventListener('resize', this.onResize);
    }

    saveLocal = () => {
        window.images = this.state.images;
        if (window.sessionStorage)
            window.sessionStorage.setItem("images", JSON.stringify(this.state.images));
    };

    onResize = () => {
        this.forceUpdate()
    };

    onFileUploaded = response => {
        if (response.error === 0) {
            let data = response.res;
            data.forEach(item => {
                if (item.accept) {
                    let exists = false;
                    this.state.images.forEach(i => {
                        if (i.filename === item.save_name)
                            exists = true;
                    });
                    if (!exists) {
                        message.success("文件已成功上传到 " + item.save_name, 1);
                        this.setState({
                            images: [...this.state.images, {
                                filename: item.save_name,
                                width: item.width,
                                height: item.height
                            }]
                        });
                        this.saveLocal()
                    } else {
                        message.info("文件 " + item.save_name + " 已存在", 1);
                    }
                    if (this.props.changeTab)
                        this.props.changeTab(item.save_name)
                }
                else
                    message.error("文件 " + item.name + " 上传失败：" + item.reject_reason);
            })
        } else {
            message.error(response.msg)
        }
    };

    render() {
        return <div>
            <Dragger name='file'
                     multiple={true}
                     action="/upload"
                     showUploadList={false}
                     onChange={ info => {
                         if (info.file.status === 'done')
                             this.onFileUploaded(info.file.response);
                         else if (info.file.status === 'error')
                             message.error("上传失败");
                     }
            }>
                <p className="ant-upload-drag-icon">
                    <Icon type="inbox" />
                </p>
                <p className="ant-upload-text">点击或拖拽上传</p>
                <p className="ant-upload-hint">支持JPG/PNG/BMP 文件大小不超过2M</p>
            </Dragger>
            <section className="images-container">
            {
                this.state.images.map(img => <div key={img.filename} className="frame" style={{
                    width: window.innerWidth < 500 ? "100%" : img.width*200/img.height,
                    flexGrow: img.width*200/img.height
                }}>
                    <div className="title" title={img.filename}>{img.filename}</div>
                    <div className="image">
                        <i style={{
                            paddingBottom: img.height/img.width*100 + "%"
                        }}/>
                        <img src={"/source/" + img.filename} alt={img.filename} onClick={() => {
                            if (this.props.changeTab)
                                this.props.changeTab(img.filename)
                        }}/>
                    </div>
                </div>)
            }
            </section>
        </div>
    }

}

export default MainTab;