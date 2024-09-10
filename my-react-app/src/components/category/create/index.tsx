import * as React from "react";
import {useForm} from "antd/es/form/Form";
import {ICategoryCreate} from "./types.ts";
import {Button, Form, Input, Modal, Row, Upload, UploadFile} from "antd";
import {Link} from "react-router-dom";
import TextArea from "antd/es/input/TextArea";
import {useState} from "react";
import {RcFile, UploadChangeParam} from "antd/es/upload";
import {PlusOutlined} from '@ant-design/icons';
import axios from "axios";

const CategoryCreatePage : React.FC = () => {
    const [form] = useForm<ICategoryCreate>();

    const [previewOpen, setPreviewOpen] = useState<boolean>(false);
    const [previewImage, setPreviewImage] = useState('');
    const [previewTitle, setPreviewTitle] = useState('');

    const onSubmitForm = async (values: ICategoryCreate) => {
        // console.log("Create category data", values);
        const url = "http://localhost:8888/api/category";
        try {
            const id = await axios.post<number>(url, values,
                {
                    headers: {
                        "Content-Type": "multipart/form-data"
                    }
                });
            console.log("Result is good id ", id);
        }
        catch(e) {
            console.log("Error", e);
        }
    }

    return (
      <>
          <div className="py-12">
              <div className="mx-auto max-w-7xl px-6 lg:px-24">
                  <p className="text-center text-3xl font-bold tracking-tight text-gray-900 sm:text-4xl">
                      Створення категорії
                  </p>
                  <Form
                    form={form}
                    onFinish={onSubmitForm}
                    layout={"vertical"}>

                      <Form.Item
                        label={"Назва"}
                        name="name"
                        htmlFor={"name"}
                        rules={[
                            {required: true, message: "Вкажіть назву категорії"}
                        ]}
                      >
                          <Input autoComplete={"name"} />
                      </Form.Item>

                      <Form.Item
                          label="Оберіть фото"
                          valuePropName="image"
                          name="image"
                          // getValueFromEvent={normFile}
                          getValueFromEvent={(e: UploadChangeParam) => {
                              const image = e?.fileList[0] as any;
                              return image?.originFileObj;
                          }}
                          rules={[{ required: true, message: "Оберіть фото" }]}>
                          <Upload
                              listType="picture-card"
                              beforeUpload={() => false}
                              accept="image/png, image/jpeg, image/webp"
                              maxCount={1}
                              onPreview={(file: UploadFile) => {
                                  if (!file.url && !file.preview) {
                                      file.preview = URL.createObjectURL(file.originFileObj as RcFile);
                                  }

                                  setPreviewImage(file.url || (file.preview as string));
                                  setPreviewOpen(true);
                                  setPreviewTitle(file.name || file.url!.substring(file.url!.lastIndexOf('/') + 1));
                              }}

                          >
                              <button style={{ border: 0, padding: 0, background: 'transparent' }} type="button" />
                              <div className='d-flex flex-column align-items-center'>
                                  <PlusOutlined />
                                  <span>Upload</span>
                              </div>

                          </Upload>
                      </Form.Item>

                      <Form.Item
                          label={"Опис"}
                          name="description"
                          htmlFor={"description"}
                          rules={[
                              {required: true, message: "Вкажіть опис категорії"}
                          ]}
                      >
                          <TextArea autoComplete={"description"} rows={3} />
                      </Form.Item>


                      <Row style={{display: 'flex', justifyContent: 'center'}}>
                          <Button style={{margin: 10}}
                                  className={"text-white capitalize transition-colors duration-300 transform bg-blue-500"}
                                  type="primary" htmlType="submit">
                              Додати
                          </Button>

                          <Button style={{margin: 10}} htmlType="button">
                              Скасувати
                          </Button>
                      </Row>

                  </Form>
              </div>
          </div>

          <Modal open={previewOpen} title={previewTitle} footer={null} onCancel={() => setPreviewOpen(false)}>
              <img alt="example" style={{width: '100%'}} src={previewImage}/>
          </Modal>
      </>
    );
}
export default CategoryCreatePage;