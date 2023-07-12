// CKEditor Use
ClassicEditor
    .create(document.querySelector('#activityInfo'), {
        extraPlugins: [CustomUploadAdapterPlugin],
        customUploadAdapter: {
            uploadUrl: 'http://localhost:8080/gymlife/activity/api/upload'
        }
    })
    .then(editor => {
        console.log(editor);
        // 將 CKEditor 實例綁定到全局變數 (一鍵上傳用)
        window.editor = editor;
    })
    .catch(error => {
        console.error(error);
    });

// 自定義上傳適配器插件
function CustomUploadAdapterPlugin(editor) {
    editor.plugins.get('FileRepository').createUploadAdapter = loader => {
        return new CustomUploadAdapter(loader, editor.config.get('customUploadAdapter.uploadUrl'));
    };
}

// 自定義上傳適配器
class CustomUploadAdapter {
    constructor(loader, uploadUrl) {
        this.loader = loader;
        this.uploadUrl = uploadUrl;
    }

    upload() {
        return this.loader.file
            .then(file => new Promise((resolve, reject) => {
                this._uploadFile(file).then(response => {
                    if (response.url) {
                        resolve({ default: response.url });
                    } else {
                        reject(`Upload failed: ${response.message}`);
                    }
                });
            }));
    }

    _uploadFile(file) {
        const data = new FormData();
        data.append('file', file);

        return fetch(this.uploadUrl, {
            method: 'POST',
            body: data
        })
            .then(response => response.json())
            .catch(error => {
                console.error('Upload error:', error);
                throw error;
            });
    }
}
